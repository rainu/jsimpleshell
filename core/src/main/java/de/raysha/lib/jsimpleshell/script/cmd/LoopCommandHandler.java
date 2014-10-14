package de.raysha.lib.jsimpleshell.script.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.raysha.lib.jsimpleshell.CommandRecorder;
import de.raysha.lib.jsimpleshell.PromptElement;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.ShellCommand;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.CLIException;
import de.raysha.lib.jsimpleshell.exception.ExitException;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.script.Environment;
import de.raysha.lib.jsimpleshell.util.MultiMap;

/**
 * This handler contains loop-commands that can be used for scripting.
 * @author rainu
 */
public class LoopCommandHandler {

	public static final String VARIABLE_NAME_FROM = "_LF";	//LOOP_FROM
	public static final String VARIABLE_NAME_UNTIL = "_LU"; //LOOP_UNTIL
	public static final String VARIABLE_NAME_STEP = "_LS"; //LOOP_STEP
	public static final String VARIABLE_NAME_COUNT = "_LC"; //LOOP_COUNT

	@Inject
	private Shell shell;

	@Inject
	private MessageResolver messageResolver;

	@Inject
	private Environment environment;

	@Command(abbrev = "command.abbrev.loop.count", description = "command.description.loop.count",
			header = "command.header.loop.count", name = "command.name.loop.count", startsSubshell = true)
	public void loopCount(
			@Param(value = "param.name.loop.count.1", description = "param.description.loop.count.1")
			Long until) throws IOException, CLIException{

		loopCount(0L, until, 1L);
	}

	@Command(abbrev = "command.abbrev.loop.count", description = "command.description.loop.count",
			header = "command.header.loop.count", name = "command.name.loop.count", startsSubshell = true)
	public void loopCount(
			@Param(value = "param.name.loop.count", description = "param.description.loop.count")
			Long from,
			@Param(value = "param.name.loop.count.1", description = "param.description.loop.count.1")
			Long until,
			@Param(value = "param.name.loop.count.2", description = "param.description.loop.count.2")
			Long step) throws IOException, CLIException{

		final LoopPrompt myPrompt = new LoopPrompt();

		Shell subshell = ShellBuilder.subshell(myPrompt, shell)
							.behavior()
								.disableExitCommand()
							.back()
						.build();


		subshell.addMainHandler(new LoopSpecialCommand(), ".");
		copyMainHandler(subshell);

		if(mainLoop()){
			CommandRecorder recorder = new CommandRecorder(subshell);

			List<String> commands = recorder.recordCommands();
			purge(commands);

			for(long i=from; i < until; i += step){
				setEnvironmentVariables(i, from, until, step);
				shell.getPipeline().prepend(commands);
			}
			setRemoveVariables();
		}else{
			//i am a sub-loop
			subshell.commandLoop();
		}
	}

	private void setRemoveVariables() {
		shell.getPipeline().prepend(
				removeGlobalVariable(VARIABLE_NAME_COUNT),
				removeGlobalVariable(VARIABLE_NAME_FROM),
				removeGlobalVariable(VARIABLE_NAME_UNTIL),
				removeGlobalVariable(VARIABLE_NAME_STEP));
	}

	private String removeGlobalVariable(String name) {
		//TODO: implement remove variables!
		return "";
	}

	private void setEnvironmentVariables(Long offset, Long from, Long until, Long step) {
		shell.getPipeline().prepend(
				setGlobalVariable(VARIABLE_NAME_COUNT, offset),
				setGlobalVariable(VARIABLE_NAME_FROM, from),
				setGlobalVariable(VARIABLE_NAME_UNTIL, until),
				setGlobalVariable(VARIABLE_NAME_STEP, step));
	}

	private String setGlobalVariable(String name, Object value) {
		final String command = "." + messageResolver.resolveGeneralMessage(EnvironmentCommandHandler.COMMAND_NAME_SET_GLOBAL_VARIABLE);
		final String varName = name + getLoopCount();

		return command + " \"" + varName + "\" \"" + String.valueOf(value) + "\"";
	}

	private Integer getLoopCount() {
		int count = 0;
		for(PromptElement part : shell.getPath()){
			if(part instanceof LoopPrompt){
				count++;
			}
		}

		return count;
	}

	private void purge(List<String> commands) {
		if(commands != null && !commands.isEmpty()){
			String lastLine = commands.get(commands.size() - 1);
			lastLine = lastLine.trim();

			if(lastLine.endsWith(".loop-end")){
				commands.remove(commands.size() - 1);
			}
		}
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

	private void copyMainHandler(Shell subshell) {
		List<Object> mainHandler = getMainHandler(shell);
		List<Object> defaultHandler = getMainHandler(ShellBuilder.shell("").build());

		mainLoop: for(Object handler : mainHandler){
			if(	handler instanceof LoopCommandHandler ||
				handler instanceof LoopSpecialCommand){

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

	public class LoopPrompt implements PromptElement {
		@Override
		public String render() {
			return messageResolver.resolveGeneralMessage("message.loop.prompt");
		}
	}

	public class LoopSpecialCommand {

		@Command(abbrev = "command.abbrev.loop.end", description = "command.description.loop.end",
				header = "command.header.loop.end", name = "command.name.loop.end")
		public void loopEnd() throws ExitException {
			throw new ExitException();
		}
	}
}
