package de.raysha.lib.jsimpleshell.script.cmd.process;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.raysha.lib.jsimpleshell.PromptElement;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.ExitException;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.util.MessagePrompt;

/**
 * This class contains all commands about starting a external process.
 *
 * @author rainu
 */
public class ProcessCommandHandler {

	@Inject
	private Shell shell;

	@Inject
	private MessageResolver messageResolver;

	@Command(abbrev = "command.abbrev.execution", description = "command.description.execution",
			header = "command.header.execution", name = "command.name.execution", displayResult = false)
	public ProcessResult execute(
			@Param(value = "param.name.execution", description = "param.description.execution")
			String command,
			@Param(value = "param.name.execution.1", description = "param.description.execution.1")
			String...arguments) throws IOException {

		ExecutionBuilder builder = new ExecutionBuilder();
		builder.setCommand(command);
		builder.setArguments(arguments);
		final Process process = builder.build();

		return waitForProcess(process, true, true);
	}

	@Command(abbrev = "command.abbrev.execution.build", description = "command.description.execution.build",
			header = "command.header.execution.build", name = "command.name.execution.build",
			displayResult = false, startsSubshell = true)
	public ProcessResult execute() throws IOException {
		PromptElement subPrompt = new MessagePrompt("message.execution.prompt", messageResolver);
		ExecutionBuilder builder = new ExecutionBuilder();

		Shell subShell = ShellBuilder.subshell(subPrompt, shell)
							.behavior()
								.addHandler(builder)
								.disableExitCommand()
							.build();

		subShell.commandLoop();
		final Process process = builder.build();
		if(process == null) return null;	//user not run the process

		return waitForProcess(process, builder.isPrintOut(), builder.isPrintErr());
	}

	private ProcessResult waitForProcess(Process process, boolean printOut, boolean printErr) throws IOException {
		ProcessController controller = new ProcessController(process, shell);
		controller.setPrintOut(printOut);
		controller.setPrintErr(printErr);

		return controller.waitForProcess();
	}

	public class ExecutionBuilder {
		private String command;
		private String[] arguments;
		private Map<String, String> env = new HashMap<String, String>();
		private File home;
		private boolean isCanceled = false;
		private boolean printOut = true;
		private boolean printErr = true;

		@Command(abbrev = "command.abbrev.execution.build.command", description = "command.description.execution.build.command",
				header = "command.header.execution.build.command", name = "command.name.execution.build.command")
		public void setCommand(
				@Param(value = "param.name.execution", description = "param.description.execution")
				String command){

			this.command = command;
		}

		@Command(abbrev = "command.abbrev.execution.build.argument", description = "command.description.execution.build.argument",
				header = "command.header.execution.build.argument", name = "command.name.execution.build.argument")
		public void setArguments(
				@Param(value = "param.name.execution.1", description = "param.description.execution.1")
				String...arguments){

			this.arguments = arguments;
		}

		@Command(abbrev = "command.abbrev.execution.build.environment", description = "command.description.execution.build.environment",
				header = "command.header.execution.build.environment", name = "command.name.execution.build.environment")
		public void addEnvironmentProperty(
				@Param(value = "param.name.execution.environment", description = "param.description.execution.environment")
				String key,
				@Param(value = "param.name.execution.environment.1", description = "param.description.execution.environment.1")
				String value){

			this.env.put(key, value);
		}

		@Command(abbrev = "command.abbrev.execution.build.environment.remove", description = "command.description.execution.build.environment.remove",
				header = "command.header.execution.build.environment.remove", name = "command.name.execution.build.environment.remove")
		public void removeEnvironmentProperty(
				@Param(value = "param.name.execution.environment.remove", description = "param.description.execution.environment.remove")
				String key){

			this.env.remove(key);
		}

		@Command(abbrev = "command.abbrev.execution.build.home", description = "command.description.execution.build.home",
				header = "command.header.execution.build.home", name = "command.name.execution.build.home")
		public void setWorkingDir(
				@Param(value = "param.name.execution.home", description = "param.description.execution.home")
				File home){

			this.home = home;
		}

		@Command(abbrev = "command.abbrev.execution.build.show", description = "command.description.execution.build.show",
				header = "command.header.execution.build.show", name = "command.name.execution.build.show")
		public String show(){
			StringBuilder args = new StringBuilder();
			StringBuilder env = new StringBuilder();
			String cmd = command == null ? "-" : command;
			String dir = home == null ? "-" : home.getAbsolutePath();
			String out = printOut ? messageResolver.resolveGeneralMessage("message.execution.show.enable") : messageResolver.resolveGeneralMessage("message.execution.show.disable");
			String err = printErr ? messageResolver.resolveGeneralMessage("message.execution.show.enable") : messageResolver.resolveGeneralMessage("message.execution.show.disable");

			if(this.arguments != null && arguments.length >= 1){
				for(String arg : this.arguments){
					args.append("\"");
					args.append(arg);
					args.append("\" ");
				}
				//remove last " "
				args.replace(args.length() - 1, args.length(), "");
			}

			if(this.env != null && this.env.size() >= 1){
				for(Entry<String, String> prop : this.env.entrySet()){
					env.append("\t");
					env.append(prop.getKey());
					env.append("=");
					env.append(prop.getValue());
					env.append("\n");
				}
				//remove last "\n"
				env.replace(env.length() - 1, env.length(), "");
			}

			String output = messageResolver.resolveGeneralMessage("message.execution.show")
						.replace("{cmd}", cmd)	//{cmd} -> command name
						.replace("{args}", args.toString())	//{args} -> arguments
						.replace("{env}", env.toString())	//{env} -> environment properties
						.replace("{dir}", dir)	//{dir} -> working directory
						.replace("{output}", out)
						.replace("{error}", err);

			return output;
		}

		@Command(abbrev = "command.abbrev.execution.build.disable.out", description = "command.description.execution.build.disable.out",
				header = "command.header.execution.build.disable.out", name = "command.name.execution.build.disable.out")
		public void disableOutput(){
			printOut = false;
		}

		@Command(abbrev = "command.abbrev.execution.build.enable.out", description = "command.description.execution.build.enable.out",
				header = "command.header.execution.build.enable.out", name = "command.name.execution.build.enable.out")
		public void enableOutput(){
			printOut = true;
		}

		@Command(abbrev = "command.abbrev.execution.build.disable.err", description = "command.description.execution.build.disable.err",
				header = "command.header.execution.build.disable.err", name = "command.name.execution.build.disable.err")
		public void disableError(){
			printErr = false;
		}

		@Command(abbrev = "command.abbrev.execution.build.enable.err", description = "command.description.execution.build.enable.err",
				header = "command.header.execution.build.enable.err", name = "command.name.execution.build.enable.err")
		public void enableError(){
			printErr = true;
		}

		@Command(abbrev = "command.abbrev.execution.build.cancel", description = "command.description.execution.build.cancel",
				header = "command.header.execution.build.cancel", name = "command.name.execution.build.cancel")
		public void cancel() throws ExitException{
			isCanceled = true;
			throw new ExitException(messageResolver.resolveGeneralMessage("message.execution.cancel"));
		}

		@Command(abbrev = "command.abbrev.execution.build.run", description = "command.description.execution.build.run",
				header = "command.header.execution.build.run", name = "command.name.execution.build.run")
		public void run() throws ExitException{
			throw new ExitException();
		}

		public Process build() throws IOException {
			if(isCanceled) {
				return null;
			}

			String[] cmdArray = getCommandArray();
			String[] envProp = getEnvironmentProperties();

			return Runtime.getRuntime().exec(cmdArray, envProp, home);
		}

		private String[] getEnvironmentProperties() {
			String[] properties = new String[env.size()];

			int i=0;
			for(Entry<String, String> prop : env.entrySet()){
				properties[i++] = prop.getKey() + "=" + prop.getValue();
			}

			return properties;
		}

		private String[] getCommandArray() {
			String[] cmdArray = new String[getArgumentSize() + 1];
			cmdArray[0] = command;
			for(int i=1; i < cmdArray.length; i++){
				cmdArray[i] = arguments[i - 1];
			}
			return cmdArray;
		}

		private int getArgumentSize() {
			if(arguments == null) return 0;

			return arguments.length;
		}

		public boolean isPrintOut() {
			return printOut;
		}

		public boolean isPrintErr() {
			return printErr;
		}
	}
}
