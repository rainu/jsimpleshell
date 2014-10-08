package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.SubShellCommands;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;

public class WithoutExit extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.addHandler(new SubShellCommands());
	}

	@Test
	public void noExit() throws IOException{
		executeAndWaitForCommand("sub-shell-without-exit");
		CommandResult result = executeAndWaitForCommand("?list-all");

		assertFalse(result.containsOutLine(".*exit.*"));
	}

	@Test
	public void typeExit() throws IOException{
		executeAndWaitForCommand("sub-shell-without-exit");
		CommandResult result = executeAndWaitForCommand("exit");

		assertTrue(result.containsErrLine("Unknown command: \\\"exit\\\""));
	}

	@Test
	public void exitAlternative() throws IOException{
		executeAndWaitForCommand("sub-shell-without-exit");
		CommandResult result = executeAndWaitForCommand("quit");

		assertTrue(result.containsOutLine("Bye\\.\\.\\."));
		assertTrue(result.getOut().endsWith("IT> "));
	}
}
