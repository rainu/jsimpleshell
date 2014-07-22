package de.raysha.lib.jsimpleshell.exception;

/**
 * This exception can be thrown by each command. This will cause that the
 * current shell will be leaved!
 * 
 * @author rainu
 */
public class ExitException extends CLIException {

	public ExitException() {
		super();
	}
	
	public ExitException(String message) {
		super(message);
	}
}
