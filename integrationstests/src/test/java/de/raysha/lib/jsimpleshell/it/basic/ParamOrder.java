package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

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
		CommandResult result = executeAndWaitForCommand("set", "--p-3", "p3", "--p1", "p1", "--p2", "true");

		assertTrue(result.toString(), result.containsOutLine("p1, true, p3"));
	}

	@Test
	public void wrongParameter() throws IOException, CLIException{
		CommandResult result = executeAndWaitForCommand("set", "--unknownParam", "unknown", "--p1", "p1", "--p2", "true");

		assertFalse(result.toString(), result.containsOutLine("p1, true, p3"));
	}

	@Test
	public void varArgs() throws IOException, CLIException{
		CommandResult result = executeAndWaitForCommand("set", "--opt", "op1", "--p1", "p1", "--opt", "op2", "--opt", "op3");

		assertTrue(result.toString(), result.getOut().contains("p1, " + Arrays.toString(new String[]{"op1", "op2", "op3"})));
	}
}
