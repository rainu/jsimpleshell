package de.raysha.lib.jsimpleshell.handler;

/**
 * Classes that want to get the access of used {@link CommandValidator} should implement this interface.
 *
 * @author rainu
 */
public interface CommandValidatorDependent {

	/**
	 * This method pass a {@link CommandValidator}.
	 *
	 * @param validator The {@link CommandValidator} instance.
	 */
	public void cliSetCommandValidator(CommandValidator validator);
}
