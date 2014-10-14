package de.raysha.lib.jsimpleshell.handler;

/**
 * Classes which implements this interface will be inform about the command line processing.
 *
 * @author rainu
 */
public interface CommandLoopObserver {

	/**
	 * This method will be called before the command line will be proceed.
	 *
	 * @param line The users input.
	 */
	public void cliBeforeCommandLine(String line);

	/**
	 * This method will be called after the command line was proceed.
	 *
	 * @param line The users input.
	 */
	public void cliAfterCommandLine(String line);
}
