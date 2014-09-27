package de.raysha.lib.jsimpleshell.exception;

/**
 * This exception is thrown if a user haven't access to the current command.
 *
 * @author rainu
 */
public class AccessDeniedException extends CLIException {
	private static final long serialVersionUID = 1L;

	public AccessDeniedException(){
		super();
	}

	public AccessDeniedException(String reason){
		super(reason);
	}
}
