package de.raysha.lib.jsimpleshell;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.exception.ExitException;

public class ExitAlternativeCommands {

	@Command
	public void quit() throws ExitException{
		throw new ExitException("Bye...");
	}
}
