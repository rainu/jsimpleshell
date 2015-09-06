package de.raysha.lib.jsimpleshell.handler;

import de.raysha.lib.jsimpleshell.annotation.Inject;

/**
 * Classes that want to get the access of used {@link CommandValidator} should implement this interface.
 *
 * @deprecated Use the {@link Inject} annotation over a setter-method instead of using this interface
 * (it has the same effect).
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
