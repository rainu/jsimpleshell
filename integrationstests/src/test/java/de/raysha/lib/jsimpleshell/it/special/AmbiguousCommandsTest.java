package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.AmbiguousCommands;
import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.CLIException;

public class AmbiguousCommandsTest extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
					.behavior()
						.addHandler(new AmbiguousCommands())
					.root();
	}

	@Test
	public void setBoolean() throws IOException, CLIException{
		executeCommand("set", "true");

		CommandResult result = waitForShell();

		assertEquals("", result.getErr());
		assertTrue(result.getOut().contains("Boolean: true"));
	}

	@Test
	public void setInteger() throws IOException, CLIException{
		executeCommand("set", "130810");

		CommandResult result = waitForShell();

		assertEquals("", result.getErr());
		assertTrue(result.getOut().contains("Integer: 130810"));
	}

	@Test
	public void setString() throws IOException, CLIException{
		executeCommand("set", "Rainu");

		CommandResult result = waitForShell();

		assertEquals("", result.getErr());
		assertTrue(result.getOut().contains("String: Rainu"));
	}

	@Test
	public void setVarBoolean() throws IOException, CLIException{
		executeCommand("set-var", "true", "false");

		CommandResult result = waitForShell();

		assertEquals("", result.getErr());
		assertTrue(result.getOut().contains("Boolean[]: [true, false]"));
	}

	@Test
	public void setVarInteger() throws IOException, CLIException{
		executeCommand("set-var", "130810", "130490");

		CommandResult result = waitForShell();

		assertEquals("", result.getErr());
		assertTrue(result.getOut().contains("Integer[]: [130810, 130490]"));
	}

	@Test
	public void setVarString() throws IOException, CLIException{
		executeCommand("set-var", "Rainu", "JSS");

		CommandResult result = waitForShell();

		assertEquals("", result.getErr());
		assertTrue(result.getOut().contains("String[]: [Rainu, JSS]"));
	}

	@Test
	public void complex() throws IOException, CLIException{
		executeCommand("complex", "String", "false");

		CommandResult result = waitForShell();

		assertEquals("", result.getErr());
		assertTrue(result.getOut().contains("complex: String, false"));
	}

	@Test
	public void complexAmbiguous() throws IOException, CLIException{
		executeCommand("complex", "true", "false");

		CommandResult result = waitForShell();

		assertTrue(result.getErr().contains("Ambiguous"));
	}
}
