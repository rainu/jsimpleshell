package de.raysha.lib.jsimpleshell.it.special;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.InputCommands;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.SubShellCommands;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class Input extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
					.behavior()
						.addHandler(new InputCommands())
						.addHandler(new SubShellCommands())
					.root();
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

	@Test
	public void ctrlDExit() throws IOException{
		executeAndWaitForCommand("new-sub-shell");
		simulateUserInput("\u0004");	//simulate CTRL D

		CommandResult result = waitForShell();
		assertFalse(result.isError());
		assertEquals("IT> ", result.getOut());
	}

	@Test
	public void ctrlDSpecialExit() throws IOException{
		executeAndWaitForCommand("sub-shell-with-special-exit");
		simulateUserInput("\u0004");	//simulate CTRL D

		CommandResult result = waitForShell();
		assertFalse(result.toString(), result.isError());
		assertEquals("Bye...\nIT> ", result.getOut());
	}
}