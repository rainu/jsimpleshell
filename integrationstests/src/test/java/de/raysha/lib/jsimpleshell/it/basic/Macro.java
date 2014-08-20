package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.SubShellCommands;
import de.raysha.lib.jsimpleshell.exception.CLIException;
import de.raysha.lib.jsimpleshell.io.TerminalIO;

public class Macro extends IntegrationsTest {

	File macroHome;

	@Override
	protected ShellBuilder buildShell() throws IOException {
		macroHome = File.createTempFile("jsimpleshell", ".macro");
		macroHome.delete();
		macroHome.mkdirs();

		return super.buildShell()
					.addHandler(new SubShellCommands())
					.setMacroHome(macroHome);
	}

	@After
	public void clean() throws IOException{
		FileUtils.deleteDirectory(macroHome);
	}

	@Test
	public void recordAndRunMacro() throws IOException, CLIException{
		recordMacro("myMacro");
		runMacro("myMacro");
	}

	private void recordMacro(String macroName) throws IOException {
		executeCommand("!start-record", macroName);
		executeCommand("?list");
		executeCommand("?list-all");
		executeCommand("unknownCommand");
		executeCommand("!stop-record");

		waitForShell();

		String makroContent = FileUtils.readFileToString(new File(macroHome, macroName + TerminalIO.MACRO_SUFFIX));
		String expectedContent = "?list\n?list-all\nunknownCommand\n";

		assertTrue("Macro wasn't created correctely! " + makroContent,
				makroContent.endsWith(expectedContent));
	}

	private void runMacro(String macroName) throws IOException {
		CommandResult result = executeAndWaitForCommand("!run-macro", macroName);

		assertTrue(result.containsOutLine("IT\\> \\?list"));
		assertTrue(result.containsOutLine("IT\\> \\?list-all"));
		assertTrue(result.containsOutLine("IT\\> unknownCommand"));
		assertFalse(result.containsOutLine("IT\\> !stop-record"));
	}

	@Test
	public void flushMakroOnExit() throws IOException, CLIException{
		executeCommand("new-sub-shell");
		executeCommand("!start-record", "myMacro");
		executeCommand("?list");
		executeCommand("?list-all");
		executeCommand("unknownCommand");
		executeCommand("exit");

		waitForShell();

		String makroContent = FileUtils.readFileToString(new File(macroHome, "myMacro" + TerminalIO.MACRO_SUFFIX));
		String expectedContent = "?list\n?list-all\nunknownCommand\nexit\n";

		assertTrue("Macro wasn't created correctely! " + makroContent,
				makroContent.endsWith(expectedContent));
	}

	@Test
	public void multiRecord() throws IOException, CLIException{
		executeCommand("!start-record", "myMacro");
		executeCommand("?list");
		executeCommand("!start-record", "myMacro");

		CommandResult result = waitForShell();

		assertTrue(result.getOut().contains("Macro recording is already started!"));
	}
}
