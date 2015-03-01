package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.OutputCommands;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;

public class Output extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
					.behavior()
						.addHandler(new OutputCommands())
					.root();
	}

	@Test
	public void outColor() throws IOException{
		CommandResult result = executeAndWaitForCommand("out-color");

		assertTrue(result.containsOutLine("\u001B\\[30mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[31mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[32mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[33mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[34mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[35mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[36mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[37mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[40mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[41mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[42mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[43mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[44mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[45mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[46mtext\u001B\\[0m"));
		assertTrue(result.containsOutLine("\u001B\\[47mtext\u001B\\[0m"));
	}

	@Test
	public void outWitoutColor() throws IOException{
		CommandResult result = executeAndWaitForCommand("out-without-color");

		assertFalse(result.containsOutLine("\u001B\\[30mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[31mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[32mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[33mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[34mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[35mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[36mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[37mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[40mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[41mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[42mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[43mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[44mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[45mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[46mtext\u001B\\[0m"));
		assertFalse(result.containsOutLine("\u001B\\[47mtext\u001B\\[0m"));
	}

	@Test
	public void errColor() throws IOException{
		CommandResult result = executeAndWaitForCommand("err-color");

		assertTrue(result.containsErrLine("\u001B\\[30mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[31mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[32mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[33mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[34mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[35mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[36mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[37mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[40mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[41mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[42mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[43mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[44mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[45mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[46mtext\u001B\\[0m"));
		assertTrue(result.containsErrLine("\u001B\\[47mtext\u001B\\[0m"));
	}

	@Test
	public void errWitoutColor() throws IOException{
		CommandResult result = executeAndWaitForCommand("err-without-color");

		assertFalse(result.containsErrLine("\u001B\\[30mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[31mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[32mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[33mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[34mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[35mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[36mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[37mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[40mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[41mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[42mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[43mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[44mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[45mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[46mtext\u001B\\[0m"));
		assertFalse(result.containsErrLine("\u001B\\[47mtext\u001B\\[0m"));
	}

	@Test
	public void doNotDisplay() throws IOException{
		CommandResult result = executeAndWaitForCommand("do-not-display");

		assertFalse(result.isError());
		assertFalse(result.containsLine("This should be not displayed!"));
	}
}
