package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.OutputCommands;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.CLIException;

public class Color extends IntegrationsTest {
	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.addHandler(new OutputCommands())
				.enableColor();
	}

	@Test
	public void disableColor() throws IOException, CLIException{
		shellInterface.getShell().disableColor();

		shellInterface.executeCommand("out-color");
		CommandResult result = shellInterface.waitForShell();

		assertTrue(result.getOut().contains("text"));
		assertFalse(result.getOut().contains("\u001B[32mtext\u001B[0m"));
	}

	@Test
	public void enableColor() throws IOException, CLIException{
		shellInterface.getShell().enableColor();

		shellInterface.executeCommand("out-color");
		CommandResult result = shellInterface.waitForShell();

		assertTrue(result.getOut().contains("\u001B[32mtext\u001B[0m"));
	}

	@Test
	public void disableErrColor() throws IOException, CLIException{
		shellInterface.getShell().disableColor();

		shellInterface.executeCommand("err-color");
		CommandResult result = shellInterface.waitForShell();

		assertTrue(result.getErr().contains("text"));
		assertFalse(result.getErr().contains("\u001B[32mtext\u001B[0m"));
	}

	@Test
	public void enableErrColor() throws IOException, CLIException{
		shellInterface.getShell().enableColor();

		shellInterface.executeCommand("err-color");
		CommandResult result = shellInterface.waitForShell();

		assertTrue(result.getErr().contains("\u001B[32mtext\u001B[0m"));
	}
}
