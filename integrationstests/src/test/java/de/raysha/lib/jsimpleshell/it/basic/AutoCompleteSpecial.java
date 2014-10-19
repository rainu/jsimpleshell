package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.CompleterCommands;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.MainHandler;
import de.raysha.lib.jsimpleshell.ParamOrderCommands;
import de.raysha.lib.jsimpleshell.SecurityCommands;

public class AutoCompleteSpecial extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
					.behavior()
						.addHandler(new CompleterCommands())
						.addHandler(new ParamOrderCommands())
						.addHandler(new SecurityCommands())
					.back();
	}

	@Test
	public void commandNameAll() throws IOException{
		simulateUserInput("\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());
		candidateIsNotShown(result, "!get-last-exception");
		candidateIsNotShown(result, "!get-macro-home");
		candidateIsNotShown(result, "!run-macro");
		candidateIsNotShown(result, "!run-script");
		candidateIsNotShown(result, "!set-display-time");
		candidateIsNotShown(result, "!set-macro-home");
		candidateIsNotShown(result, "!start-record");
		candidateIsNotShown(result, "!stop-record");
		candidateIsNotShown(result, "?generate-HTML-help");
		candidateIsNotShown(result, "?help");
		candidateIsNotShown(result, "?list");
		candidateIsNotShown(result, "?list-all");
		candidateIsNotShown(result, ".lvar");
		candidateIsNotShown(result, ".gvar");
		candidateIsShown(result, "exit");
		candidateIsShown(result, MainHandler.SHUTDOWN);
	}

	@Test
	public void commandNameAllAfterPrefix() throws IOException{
		simulateUserInput("!\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());
		candidateIsShown(result, "!get-last-exception");
		candidateIsShown(result, "!get-macro-home");
		candidateIsShown(result, "!run-macro");
		candidateIsShown(result, "!run-script");
		candidateIsShown(result, "!set-display-time");
		candidateIsShown(result, "!set-macro-home");
		candidateIsShown(result, "!start-record");
		candidateIsShown(result, "!stop-record");
		candidateIsNotShown(result, "?generate-HTML-help");
		candidateIsNotShown(result, "?help");
		candidateIsNotShown(result, "?list");
		candidateIsNotShown(result, "?list-all");
		candidateIsNotShown(result, ".lvar");
		candidateIsNotShown(result, ".gvar");
	}

	@Test
	public void commandNameAllAfterConcat() throws IOException{
		simulateUserInput("?help && \t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());
		candidateIsNotShown(result, "!get-last-exception");
		candidateIsNotShown(result, "!get-macro-home");
		candidateIsNotShown(result, "!run-macro");
		candidateIsNotShown(result, "!run-script");
		candidateIsNotShown(result, "!set-display-time");
		candidateIsNotShown(result, "!set-macro-home");
		candidateIsNotShown(result, "!start-record");
		candidateIsNotShown(result, "!stop-record");
		candidateIsNotShown(result, "?generate-HTML-help");
		candidateIsNotShown(result, "?list");
		candidateIsNotShown(result, "?list-all");
		candidateIsNotShown(result, ".lvar");
		candidateIsNotShown(result, ".gvar");
		candidateIsShown(result, "exit");
		candidateIsShown(result, MainHandler.SHUTDOWN);
	}

	@Test
	public void commandNameAllAfterConcatAndPrefix() throws IOException{
		simulateUserInput("?help && !\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());
		candidateIsShown(result, "!get-last-exception");
		candidateIsShown(result, "!get-macro-home");
		candidateIsShown(result, "!run-macro");
		candidateIsShown(result, "!run-script");
		candidateIsShown(result, "!set-display-time");
		candidateIsShown(result, "!set-macro-home");
		candidateIsShown(result, "!start-record");
		candidateIsShown(result, "!stop-record");
		candidateIsNotShown(result, "?generate-HTML-help");
		candidateIsNotShown(result, "?list");
		candidateIsNotShown(result, "?list-all");
		candidateIsNotShown(result, ".lvar");
		candidateIsNotShown(result, ".gvar");
	}

	@Test
	public void commandNameCompletion() throws IOException{
		simulateUserInput("!get-\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());
		candidateIsShown(result, "!get-last-exception");
		candidateIsShown(result, "!get-macro-home");
		candidateIsNotShown(result, "!run-macro");
		candidateIsNotShown(result, "!run-script");
		candidateIsNotShown(result, "!set-display-time");
		candidateIsNotShown(result, "!set-macro-home");
		candidateIsNotShown(result, "!start-record");
		candidateIsNotShown(result, "!stop-record");
		candidateIsNotShown(result, "?generate-HTML-help");
		candidateIsNotShown(result, "?help");
		candidateIsNotShown(result, "?list");
		candidateIsNotShown(result, "?list-all");
		candidateIsNotShown(result, ".lvar");
		candidateIsNotShown(result, ".gvar");
		candidateIsNotShown(result, "exit");
		candidateIsNotShown(result, MainHandler.SHUTDOWN);
	}

	@Test
	public void commandNameCompletionAfterConcat() throws IOException{
		simulateUserInput("set && !get-\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());
		candidateIsShown(result, "!get-last-exception");
		candidateIsShown(result, "!get-macro-home");
		candidateIsNotShown(result, "!run-macro");
		candidateIsNotShown(result, "!run-script");
		candidateIsNotShown(result, "!set-display-time");
		candidateIsNotShown(result, "!set-macro-home");
		candidateIsNotShown(result, "!start-record");
		candidateIsNotShown(result, "!stop-record");
		candidateIsNotShown(result, "?generate-HTML-help");
		candidateIsNotShown(result, "?help");
		candidateIsNotShown(result, "?list");
		candidateIsNotShown(result, "?list-all");
		candidateIsNotShown(result, ".lvar");
		candidateIsNotShown(result, ".gvar");
		candidateIsNotShown(result, "exit");
		candidateIsNotShown(result, MainHandler.SHUTDOWN);
	}

	@Test
	public void commandNameUniqueCompletionAfterConcat() throws IOException{
		simulateUserInput("set && ?h\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());
		candidateIsShown(result, "?help");
		candidateIsNotShown(result, "!get-last-exception");
		candidateIsNotShown(result, "!get-macro-home");
		candidateIsNotShown(result, "!run-macro");
		candidateIsNotShown(result, "!run-script");
		candidateIsNotShown(result, "!set-display-time");
		candidateIsNotShown(result, "!set-macro-home");
		candidateIsNotShown(result, "!start-record");
		candidateIsNotShown(result, "!stop-record");
		candidateIsNotShown(result, "?generate-HTML-help");
		candidateIsNotShown(result, "?list");
		candidateIsNotShown(result, "?list-all");
		candidateIsNotShown(result, ".lvar");
		candidateIsNotShown(result, ".gvar");
		candidateIsNotShown(result, "exit");
		candidateIsNotShown(result, MainHandler.SHUTDOWN);
	}

	@Test
	public void commandNameArgumentCompleter() throws IOException{
		simulateUserInput("?help \t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());
		candidateIsNotShown(result, "!get-last-exception");
		candidateIsNotShown(result, "!get-macro-home");
		candidateIsNotShown(result, "!run-macro");
		candidateIsNotShown(result, "!run-script");
		candidateIsNotShown(result, "!set-display-time");
		candidateIsNotShown(result, "!set-macro-home");
		candidateIsNotShown(result, "!start-record");
		candidateIsNotShown(result, "!stop-record");
		candidateIsNotShown(result, "?generate-HTML-help");
		candidateIsNotShown(result, "?list");
		candidateIsNotShown(result, "?list-all");
		candidateIsShown(result, "exit");
		candidateIsShown(result, MainHandler.SHUTDOWN);
	}

	@Test
	public void commandNameArgumentCompleterAfterPrefix() throws IOException{
		simulateUserInput("?help !\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());
		candidateIsShown(result, "!get-last-exception");
		candidateIsShown(result, "!get-macro-home");
		candidateIsShown(result, "!run-macro");
		candidateIsShown(result, "!run-script");
		candidateIsShown(result, "!set-display-time");
		candidateIsShown(result, "!set-macro-home");
		candidateIsShown(result, "!start-record");
		candidateIsShown(result, "!stop-record");
		candidateIsNotShown(result, "?generate-HTML-help");
		candidateIsNotShown(result, "?list");
		candidateIsNotShown(result, "?list-all");
	}

	@Test
	public void commandNameArgumentCompleterAfterConcat() throws IOException{
		simulateUserInput("set && ?help \t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());
		candidateIsNotShown(result, "!get-last-exception");
		candidateIsNotShown(result, "!get-macro-home");
		candidateIsNotShown(result, "!run-macro");
		candidateIsNotShown(result, "!run-script");
		candidateIsNotShown(result, "!set-display-time");
		candidateIsNotShown(result, "!set-macro-home");
		candidateIsNotShown(result, "!start-record");
		candidateIsNotShown(result, "!stop-record");
		candidateIsNotShown(result, "?generate-HTML-help");
		candidateIsNotShown(result, "?list");
		candidateIsNotShown(result, "?list-all");
		candidateIsShown(result, "exit");
		candidateIsShown(result, MainHandler.SHUTDOWN);
	}

	@Test
	public void commandNameArgumentCompleterAfterConcatAndPrefix() throws IOException{
		simulateUserInput("set && ?help !\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());
		candidateIsShown(result, "!get-last-exception");
		candidateIsShown(result, "!get-macro-home");
		candidateIsShown(result, "!run-macro");
		candidateIsShown(result, "!run-script");
		candidateIsShown(result, "!set-display-time");
		candidateIsShown(result, "!set-macro-home");
		candidateIsShown(result, "!start-record");
		candidateIsShown(result, "!stop-record");
		candidateIsNotShown(result, "?generate-HTML-help");
		candidateIsNotShown(result, "?list");
		candidateIsNotShown(result, "?list-all");
	}

	private void candidateIsShown(CommandResult result, String candidate) {
		assertTrue("Candidate '" + candidate + "' is not shown!",
				result.getOut().contains(candidate));
	}

	private void candidateIsNotShown(CommandResult result, String candidate) {
		assertFalse("Candidate '" + candidate + "' is shown!",
				result.getOut().contains(candidate));
	}
}
