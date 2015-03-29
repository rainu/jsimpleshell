package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.ValidationCommands;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;

public class Validation extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
					.behavior()
						.addHandler(new ValidationCommands())
					.root();
	}

	@Test
	public void patternMismatch() throws IOException{
		CommandResult result = executeAndWaitForCommand("validate", "test");

		assertTrue(result.isError());
		assertTrue(result.toString(), result.containsErrLine("p1: must match.*"));
	}
}
