package de.raysha.lib.jsimpleshell.builder;

import de.raysha.lib.jsimpleshell.Shell;

/**
 * This interface contains all possible sub-builders.
 *
 * @author rainu
 */
public interface Builder {

	/**
	 * Go to the root {@link ShellBuilder}.
	 *
	 * @return The {@link ShellBuilder}
	 */
	public ShellBuilder root();

	/**
	 * Enter the part that is responsible for configuring the look of the shell.
	 *
	 * @return The {@link Look}.
	 */
	public Look look();

	/**
	 * Enter the part that is responsible for configuring the behavior of the shell.
	 *
	 * @return The {@link Behavior}.
	 */
	public Behavior behavior();

	/**
	 * Enter the part that is responsible for configuring the input/output of the shell.
	 *
	 * @return The {@link IO}.
	 */
	public IO io();

	/**
	 * Build the shell with the settings which you have set before.
	 *
	 * @return A ready-to-use {@link Shell}.
	 */
	public Shell build();
}
