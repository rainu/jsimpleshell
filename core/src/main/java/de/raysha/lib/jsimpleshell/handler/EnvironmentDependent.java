package de.raysha.lib.jsimpleshell.handler;

import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.script.Environment;

/**
 * Classes that want to get the access of used {@link Environment} should implement this interface.
 *
 * @deprecated Use the {@link Inject} annotation over a setter-method instead of using this interface
 * (it has the same effect).
 *
 * @author rainu
 */
public interface EnvironmentDependent {

	/**
	 * This method pass a {@link Environment}.
	 *
	 * @param environment The {@link Environment} instance.
	 */
	public void cliSetEnvironment(Environment environment);
}
