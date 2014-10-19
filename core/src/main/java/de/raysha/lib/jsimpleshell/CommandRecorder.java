package de.raysha.lib.jsimpleshell;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.raysha.lib.jsimpleshell.exception.ExitException;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision.Decision;
import de.raysha.lib.jsimpleshell.handler.CommandLoopObserver;
import de.raysha.lib.jsimpleshell.script.Variable;

/**
 * This class is responsible for recording command lines without execute them.
 *
 * @author rainu
 */
public class CommandRecorder implements CommandAccessManager, CommandLoopObserver {
	public static final String VARIABLE_NAME_RECORD_MODE = "_RECORD_MODE";

	private final Shell shell;
	private final List<String> commands = new LinkedList<String>();
	private boolean inCommandLoop = false;

	public CommandRecorder(Shell shell) {
		this.shell = shell;
	}

	/**
	 * Record all commands until my shell is exited.
	 *
	 * @return A list of all entered command-lines.
	 * @throws IOException For reasons look at {@link Shell#commandLoop()}
	 */
	public List<String> recordCommands() throws IOException{
		shell.addAuxHandler(this, "");

		inCommandLoop = true;

		try{
			inserRecordModeVariable();
			shell.commandLoop();
		}finally{
			removeRecordModeVariable();
			inCommandLoop = false;
		}

		return commands;
	}

	private void inserRecordModeVariable() {
		shell.getEnvironment().setVariable(new Variable(VARIABLE_NAME_RECORD_MODE, true, true));
	}

	private void removeRecordModeVariable() {
		shell.getEnvironment().setVariable(new Variable(VARIABLE_NAME_RECORD_MODE, false, true));
	}

	/**
	 * Checks whether the given shell is in record mode.
	 * In record mode no persistent actions should be done!
	 *
	 * @param shell The shell to check.
	 * @return True if the shell is in record mode. Otherwise false.
	 */
	public static boolean isShellInRecordMode(Shell shell){
		Variable var = shell.getEnvironment().getVariable(VARIABLE_NAME_RECORD_MODE);
		if(var == null){
			return false;
		}

		return (Boolean)var.getValue();
	}

	@Override
	public AccessDecision checkCommandPermission(Context context) {
		if(	!inCommandLoop ||
			isExitCommand(context.getCommand()) ||
			isSubshellCommand(context.getCommand())){

			return new AccessDecision(Decision.ALLOWED);
		}

		return new AccessDecision(Decision.MUTE, "This shell is in record mode!");
	}

	private boolean isExitCommand(ShellCommand command) {
		Class<?>[] exceptionTypes = command.getMethod().getExceptionTypes();

		if(exceptionTypes != null) for(Class<?> type : exceptionTypes){
			if(ExitException.class.isAssignableFrom(type)){
				return true;
			}
		}

		return false;
	}

	private boolean isSubshellCommand(ShellCommand command) {
		return command.startsSubshell();
	}

	@Override
	public void cliBeforeCommandLine(String line) {
		commands.add(line);
	}

	@Override
	public void cliAfterCommandLine(String line) {
		//do nothing
	}
}
