package de.raysha.lib.jsimpleshell.handler;

import de.raysha.lib.jsimpleshell.annotation.Inject;

/**
 * Classes that want to get the access of used {@link CommandAccessManager} should implement this interface.
 *
 * @deprecated Use the {@link Inject} annotation over a setter-method instead of using this interface
 * (it has the same effect).
 *
 * @author rainu
 */
public interface CommandAccessManagerDependent {

	/**
	 * This method pass a {@link CommandAccessManager}.
	 *
	 * @param accessManager The {@link CommandAccessManager} instance.
	 */
	public void cliSetCommandAccessManager(CommandAccessManager accessManager);
}
