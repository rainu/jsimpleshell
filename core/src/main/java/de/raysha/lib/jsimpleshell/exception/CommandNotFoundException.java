package de.raysha.lib.jsimpleshell.exception;

import de.raysha.lib.jsimpleshell.Token;

/**
 * This Exception will be thrown if an command can not be found.
 * 
 * @author rainu
 */
public class CommandNotFoundException extends CLIException {
	private static final long serialVersionUID = 1L;

	public CommandNotFoundException(String commandName) {
		super("Unknown command: " + Token.escapeString(commandName));
	}
	
	public CommandNotFoundException(String commandName, int argCount, boolean isAmbiguous) {
		super(	isAmbiguous ? 
				"Ambiguous command " + Token.escapeString(commandName) + " taking " + argCount + " arguments" :
				"There's no command " + Token.escapeString(commandName) + " taking " + argCount + " arguments");
	}
}
