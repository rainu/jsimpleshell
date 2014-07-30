package de.raysha.lib.jsimpleshell.handler;

import de.raysha.lib.jsimpleshell.ShellCommand;

/**
 * Classes that want to inform about command executing should implement this interface.
 *
 * @author rainu
 */
public interface CommandHookDependent {

	/**
	 * This method will be called before a command will be executed.
	 * 
	 * @param command Which command will be executed.
	 */
	public void cliBeforeCommand(ShellCommand command);
	
	/**
	 * This method will be called after a command was executed.
	 * 
	 * @param command Which command was executed.
	 * @param result The result that the command returned
	 * @param executionTime The duration of command execution 
	 */
	public void cliAfterCommand(ShellCommand command, Object result, long executionTime);
}
