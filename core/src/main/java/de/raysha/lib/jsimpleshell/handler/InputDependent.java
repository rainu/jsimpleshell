package de.raysha.lib.jsimpleshell.handler;

import de.raysha.lib.jsimpleshell.io.InputBuilder;

/**
 * Classes that want to get (user-)input within a command should implement this interface.
 *
 * @author rainu
 */
public interface InputDependent {

	/**
	 * This method pass a {@link InputBuilder}. After them the object can use that builder to get the (user-)input.
	 * 
	 * @param input The {@link InputBuilder} instance.
	 */
	public void cliSetInput(InputBuilder input);
}
