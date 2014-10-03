package de.raysha.lib.jsimpleshell.handler;

import de.raysha.lib.jsimpleshell.script.Environment;

/**
 * Classes that want to get the access of used {@link Environment} should implement this interface.
 *
 * @author rainu
 */
public interface EnvironmentDependent {

	/**
	 * This method pass a {@link Environment}.
	 *
	 * @param resolver The {@link Environment} instance.
	 */
	public void cliSetEnvironment(Environment environment);
}
