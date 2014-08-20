package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.InputCommands;
import de.raysha.lib.jsimpleshell.ShellBuilder;

public class Input extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
					.addHandler(new InputCommands());
	}

	@Test
	public void masked() throws IOException{
		executeCommand("masked");
		simulateUserInput("01234\n");

		CommandResult result = waitForShell();
		assertFalse(result.isError());

		assertTrue(result.containsOutLine("Enter: \\*\\*\\*\\*\\*"));
		assertTrue(result.containsOutLine("Password: 01234"));
	}

	@Test
	public void invisible() throws IOException{
		executeCommand("invisible");
		simulateUserInput("01234\n");

		CommandResult result = waitForShell();
		assertFalse(result.isError());

		assertTrue(result.containsOutLine("Enter: $"));
		assertTrue(result.containsOutLine("Password: 01234"));
	}

	@Test
	public void visible() throws IOException{
		executeCommand("visible");
		simulateUserInput("01234\n");

		CommandResult result = waitForShell();
		assertFalse(result.isError());

		assertTrue(result.containsOutLine("Enter: 01234$"));
		assertTrue(result.containsOutLine("Password: 01234"));
	}
}
