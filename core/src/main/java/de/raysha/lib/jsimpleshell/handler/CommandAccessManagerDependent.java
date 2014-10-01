package de.raysha.lib.jsimpleshell.handler;

/**
 * Classes that want to get the access of used {@link CommandAccessManager} should implement this interface.
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
