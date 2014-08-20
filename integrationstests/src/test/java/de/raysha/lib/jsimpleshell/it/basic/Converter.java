package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.ConverterCommands;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.ShellBuilder;

public class Converter extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
					.addHandler(new ConverterCommands());
	}

	@Test
	public void outputConvert() throws IOException{
		CommandResult result = executeAndWaitForCommand("to-entry", "KEY", "VALUE");

		assertTrue(result.containsOutLine("KEY;VALUE"));
	}

	@Test
	public void inputConvert() throws IOException{
		CommandResult result = executeAndWaitForCommand("from-entry", "KEY;VALUE");

		assertTrue(result.containsOutLine("KEY =\\> VALUE"));
	}
}
