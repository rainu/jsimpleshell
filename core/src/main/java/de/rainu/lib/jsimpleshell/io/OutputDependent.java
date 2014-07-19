package de.rainu.lib.jsimpleshell.io;

/**
 * Classes that want to make an output within a command should implement this interface.
 *
 * @author rainu
 */
public interface OutputDependent {

	/**
	 * This method pass a {@link OutputBuilder}. After them the object can use that builder to make an output.
	 * 
	 * @param output The {@link OutputBuilder} instance.
	 */
	public void cliSetOutput(OutputBuilder output);
}
