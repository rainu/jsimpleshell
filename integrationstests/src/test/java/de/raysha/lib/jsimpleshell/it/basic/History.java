package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.InputCommands;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;

public class History extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.behavior()
					.addHandler(new InputCommands())
				.root();
	}

	@Test
	public void moveThrough() throws IOException{
		executeCommand("ABC");
		executeCommand("DEF");
		executeCommand("GHI");
		waitForShellCommandExec();

		movePrev();
		isLineShown(waitForShell(), "GHI");
		movePrev();
		isLineShown(waitForShell(), "DEF");
		movePrev();
		isLineShown(waitForShell(), "ABC");

		moveNext();
		isLineShown(waitForShell(), "DEF");
		moveNext();
		isLineShown(waitForShell(), "GHI");
	}

	@Test
	public void searchForward() throws IOException{
		executeCommand("JKL");
		executeCommand("MNO");
		executeCommand("PQR");
		waitForShellCommandExec();

		searchForward("J\n");
		isLineShown(waitForShell(), "JKL");
	}

	@Test
	public void noHistoryForCustomInput() throws IOException{
		executeCommand("visible");
		simulateUserInput("PW\n");

		waitForShellCommandExec();
		movePrev();
		isLineShown(waitForShell(), "visible");
	}

	private void isLineShown(CommandResult result, String line) {
		assertTrue("The command-line '" + line + "' is not shown!" + result,
				result.containsOutLine(".*" + line + ".*"));
	}

	private void searchForward(String string) throws IOException {
		simulateUserInput(String.valueOf((char)18));
		simulateUserInput(string);
	}

	private void movePrev() throws IOException {
		simulateUserInput("\033[A");
	}

	private void moveNext() throws IOException {
		simulateUserInput("\033[B");
	}
}
