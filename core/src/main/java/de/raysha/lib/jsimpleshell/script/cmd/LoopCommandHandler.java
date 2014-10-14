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
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.CLIException;
import de.raysha.lib.jsimpleshell.exception.ExitException;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.util.MultiMap;

/**
 * This handler contains loop-commands that can be used for scripting.
 * @author rainu
 */
public class LoopCommandHandler {

	@Inject
	private Shell shell;

	@Inject
	private MessageResolver messageResolver;

	@Command(startsSubshell = true)
	public void loop() throws IOException, CLIException{
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

			shell.getPipeline().prepend(commands);
			shell.getPipeline().prepend(commands);
		}else{
			//i am a sub-loop
			subshell.commandLoop();
		}
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
		List<Object> mainHandler = getMainHandler();
		for(Object handler : mainHandler){
			if(	handler instanceof LoopCommandHandler ||
				handler instanceof LoopSpecialCommand){

				continue;
			}

			for(ShellCommand cmd : shell.getCommandTable().getCommandTable()){
				if(cmd.getHandler() == handler){
					subshell.addMainHandler(handler, cmd.getPrefix());
					break;
				}
			}
		}
	}

	private List<Object> getMainHandler() {
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
			return "loop";
		}
	}

	public class LoopSpecialCommand {
		@Command
		public void loopEnd() throws ExitException {
			throw new ExitException();
		}
	}
}
