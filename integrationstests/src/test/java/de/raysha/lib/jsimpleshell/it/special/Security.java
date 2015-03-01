package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.SecurityCommands;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;

public class Security extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.behavior()
					.addHandler(new SecurityCommands())
				.root();
	}

	@Test
	public void loginBefore() throws IOException{
		executeAndWaitForCommand("login");
		CommandResult result = executeAndWaitForCommand("do-something");

		assertTrue(result.containsOutLine("^Read private messages.*"));
	}

	@Test
	public void noLogin() throws IOException{
		CommandResult result = executeAndWaitForCommand("do-something");

		assertFalse(result.containsOutLine("^Read private messages.*"));
		assertTrue(result.containsErrLine(".*You are not logged in!"));
	}
}
