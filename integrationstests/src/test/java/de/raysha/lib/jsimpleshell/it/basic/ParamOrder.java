package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.ParamOrderCommands;
import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.CLIException;

public class ParamOrder extends IntegrationsTest {
	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.addHandler(new ParamOrderCommands());
	}

	@Test
	public void order() throws IOException, CLIException{
		CommandResult result = executeAndWaitForCommand("set", "--p3", "p3", "--p1", "p1", "--p2", "true");

		assertTrue(result.toString(), result.containsOutLine("p1, true, p3"));
	}
}
