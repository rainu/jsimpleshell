package de.raysha.lib.jsimpleshell.script.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.raysha.lib.jsimpleshell.CommandRecorder;
import de.raysha.lib.jsimpleshell.PromptElement;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.Shell.ExitCommand;
import de.raysha.lib.jsimpleshell.ShellCommand;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.ExitException;
import de.raysha.lib.jsimpleshell.handler.CommandLoopObserver;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.script.Environment;
import de.raysha.lib.jsimpleshell.script.Variable;
import de.raysha.lib.jsimpleshell.script.cmd.LoopState.LoopExecution;
import de.raysha.lib.jsimpleshell.util.MessagePrompt;
import de.raysha.lib.jsimpleshell.util.MultiMap;

/**
 * This handler contains loop-commands that can be used for scripting.
 * @author rainu
 */
public class LoopCommandHandler implements CommandLoopObserver {
	public static final String VARIABLE_NAME_FROM = "_LF";	//LOOP_FROM
	public static final String VARIABLE_NAME_UNTIL = "_LU"; //LOOP_UNTIL
	public static final String VARIABLE_NAME_STEP = "_LS"; //LOOP_STEP
	public static final String VARIABLE_NAME_COUNTER = "_LC"; //LOOP_COUNTER

	@Inject
	private Shell shell;

	@Inject
	private MessageResolver messageResolver;

	@Inject
	private Environment environment;

	private final LinkedList<LoopState> states = new LinkedList<LoopState>();

	@Command(abbrev = "command.abbrev.loop.count", description = "command.description.loop.count",
			header = "command.header.loop.count", name = "command.name.loop.count", startsSubshell = true)
	public void loopCount(
			@Param(value = "param.name.loop.count.1", description = "param.description.loop.count.1")
			Long until) throws IOException{

		loopCount(1L, until, 1L);
	}

	@Command(abbrev = "command.abbrev.loop.count", description = "command.description.loop.count",
			header = "command.header.loop.count", name = "command.name.loop.count", startsSubshell = true)
	public void loopCount(
			@Param(value = "param.name.loop.count", description = "param.description.loop.count")
			Long from,
			@Param(value = "param.name.loop.count.1", description = "param.description.loop.count.1")
			Long until,
			@Param(value = "param.name.loop.count.2", description = "param.description.loop.count.2")
			Long step) throws IOException {

		Map<String, Object> loopVars = new HashMap<String, Object>();
		loopVars.put(VARIABLE_NAME_FROM, from);
		loopVars.put(VARIABLE_NAME_UNTIL, until);
		loopVars.put(VARIABLE_NAME_STEP, step);

		LinkedList<Long> indexes = new LinkedList<Long>();
		for(long i=from; i <= until; i += step){
			indexes.add(i);
		}

		LoopState state = new LoopState(
				indexes.iterator(),
				loopVars,
				VARIABLE_NAME_COUNTER,
				messageResolver);

		loop(state);
	}

	@Command(abbrev = "command.abbrev.loop.each", description = "command.description.loop.each",
			header = "command.header.loop.each", name = "command.name.loop.each", startsSubshell = true)
	public void loopEach(
			@Param(value = "param.name.loop.each", description = "param.description.loop.each")
			String...items) throws IOException {

		loopIterable(Arrays.asList(items));
	}

	@Command(abbrev = "command.abbrev.loop.each", description = "command.description.loop.each",
			header = "command.header.loop.each", name = "command.name.loop.each", startsSubshell = true)
	public void loopIterable(
			@Param(value = "param.name.loop.each", description = "param.description.loop.each")
			Iterable<? extends Object> iterable) throws IOException {

		loopIterator(iterable.iterator());
	}

	@Command(abbrev = "command.abbrev.loop.each", description = "command.description.loop.each",
			header = "command.header.loop.each", name = "command.name.loop.each", startsSubshell = true)
	public void loopIterator(
			@Param(value = "param.name.loop.each", description = "param.description.loop.each")
			Iterator<? extends Object> iterator) throws IOException {

		LoopState state = new LoopState(
				iterator,
				null,
				VARIABLE_NAME_COUNTER,
				messageResolver);

		loop(state);
	}

	private void loop(LoopState state) throws IOException{
		final LoopPrompt myPrompt = new LoopPrompt(messageResolver);

		Shell subshell = ShellBuilder.subshell(myPrompt, shell)
							.behavior()
								.disableExitCommand()
							.build();


		subshell.addMainHandler(new LoopSpecialCommand(), ".");
		copyMainHandler(subshell);

		if(mainLoop()){
			//to auto complete in record mode
			putEmptyVariablesInEnvironment(state);

			CommandRecorder recorder = new CommandRecorder(subshell);

			List<String> userCommands = recorder.recordCommands();
			purge(userCommands);

			state.setCommands(userCommands);
			states.add(state);

			//the loop observer hook ensures that the loop commands
			//will be prepend to the shell's pipeline
			if(state.hasNext()){
				addNextIterationToPipeline();
			}
		}else{
			//i am a sub-loop
			subshell.commandLoop();
		}

		shell.refreshHandlerDependencies();
	}

	@Override
	public void cliBeforeCommandLine(String line) {
		//do nothing
	}

	@Override
	public void cliAfterCommandLine(String line) {
		final LoopState currentState = getCurrentState();

		if(currentState != null && currentState.hasNext()){
			//check if the line is the end of a previous loop-iteration
			if(isLineTheEndOfIteration(line, currentState)){
				//if we are at the end of a loop-iteration
				//we must add the next loop-iteration to the shell's pipeline
				addNextIterationToPipeline();
			}
		}

		//empty states must be removed
		if(currentState != null && !currentState.hasNext()){
			states.removeLast();
		}
	}

	private boolean isLineTheEndOfIteration(String line, LoopState state) {
		return line.endsWith(generateLoopEndMark(state.getUniqId()));
	}

	private LoopState getCurrentState(){
		if(states.isEmpty()) return null;

		return states.getLast();
	}

	private void putEmptyVariablesInEnvironment(LoopState state) {
		if(state.getStateVariableName() != null){
			environment.setVariable(new Variable(state.getStateVariableName(), null, true));
		}

		for(String varName : state.getStaticVariables().keySet()){
			environment.setVariable(new Variable(varName, null, true));
		}
	}

	private void copyMainHandler(Shell subshell) {
		List<Object> mainHandler = getMainHandler(shell);
		List<Object> defaultHandler = getMainHandler(ShellBuilder.shell("").build());

		for(Object defHandler : defaultHandler){
			if(defHandler instanceof ExitCommand){
				subshell.addMainHandler(new MockedExitCommand(), "");
				break;
			}
		}

		mainLoop: for(Object handler : mainHandler){
			if(handler instanceof LoopSpecialCommand){
				continue;
			}
			for(Object defHandler : defaultHandler){
				if(handler.getClass() == defHandler.getClass()){
					//the current handler is an default handler!
					continue mainLoop;
				}
			}

			for(ShellCommand cmd : shell.getCommandTable().getCommandTable()){
				if(cmd.getHandler() == handler){
					subshell.addMainHandler(handler, cmd.getPrefix());
					break;
				}
			}
		}
	}

	private void purge(List<String> commands) {
		final String name = getCommandNameForLoopEnd();
		final String abbrev = getCommandAbbrevForLoopEnd();

		if(commands != null && !commands.isEmpty()){
			String lastLine = commands.get(commands.size() - 1);
			lastLine = lastLine.trim();

			if(	lastLine.trim().endsWith(name) ||
				lastLine.trim().endsWith(abbrev)){

				commands.remove(commands.size() - 1);
			}
		}
	}

	private void addNextIterationToPipeline() {
		final LoopState currentState = getCurrentState();

		LoopExecution loopExec = currentState.next();
		List<String> commands = loopExec.getCommands();
		mark(commands, currentState.getUniqId());

		shell.getPipeline().prepend(commands);
	}

	private void mark(List<String> userCommands, Object uniqId) {
		if(userCommands == null || userCommands.isEmpty()) return;

		//we must mark the last line that we know later that this command is
		//the last recorded command of this loop instance!
		String lastCommand = userCommands.get(userCommands.size() - 1);
		lastCommand += generateLoopEndMark(uniqId);

		userCommands.remove(userCommands.size() - 1);
		userCommands.add(lastCommand);
	}

	private String generateLoopEndMark(Object uniqId) {
		return "#loop_end[" + uniqId + "]"; //use comment function as mark
	}

	private String getCommandAbbrevForLoopEnd() {
		return "." + messageResolver.resolveGeneralMessage(LoopSpecialCommand.COMMAND_ABBREV_LOOP_END);
	}

	private String getCommandNameForLoopEnd() {
		return "." + messageResolver.resolveGeneralMessage(LoopSpecialCommand.COMMAND_NAME_LOOP_END);
	}

	private boolean mainLoop() {
		//if in my "shell-chain" is an another LoopPrompt than
		//i am not the (only and the one) main loop!
		for(PromptElement part : shell.getPath()){
			if(part instanceof LoopPrompt){
				return false;
			}
		}

		return true;
	}

	private List<Object> getMainHandler(Shell shell) {
		List<Object> aux = getAuxHandler();
		List<Object> all = shell.getAllHandler();

		List<Object> result = new ArrayList<Object>(all);
		result.removeAll(aux);

		return result;
	}

	private List<Object> getAuxHandler() {
		List<Object> result = new LinkedList<Object>();

		MultiMap<String, Object> handler = shell.getSettings().getAuxHandlers();
		for(String key : handler.keySet()){
			result.addAll(handler.get(key));
		}

		return result;
	}

	public static class LoopPrompt extends MessagePrompt {
		public LoopPrompt(MessageResolver messageResolver) {
			super("message.loop.prompt", messageResolver);
		}
	}

	public class LoopSpecialCommand {
		public static final String COMMAND_ABBREV_LOOP_END = "command.abbrev.loop.end";
		public static final String COMMAND_NAME_LOOP_END = "command.name.loop.end";

		@Command(abbrev = COMMAND_ABBREV_LOOP_END, description = "command.description.loop.end",
				header = "command.header.loop.end", name = COMMAND_NAME_LOOP_END)
		public void loopEnd() throws ExitException {
			throw new ExitException();
		}
	}

	public static class MockedExitCommand {

		@Command(abbrev = ExitCommand.COMMAND_ABBREV_EXIT, description = ExitCommand.COMMAND_DESCRIPTION_EXIT,
				header = ExitCommand.COMMAND_HEADER_EXIT, name = ExitCommand.COMMAND_NAME_EXIT)
		public void exit(){
			//do nothing (it exists only for (loop-)record mode)
		}
	}
}
