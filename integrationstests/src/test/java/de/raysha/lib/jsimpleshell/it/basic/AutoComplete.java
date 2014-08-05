package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IT;
import de.raysha.lib.jsimpleshell.MainHandler;

public class AutoComplete extends IT {

	@Test
	public void commandNameAll() throws IOException{
		simulateUserInput("\t");
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
		candidateIsShown(result, "?generate-HTML-help");
		candidateIsShown(result, "?help");
		candidateIsShown(result, "?list");
		candidateIsShown(result, "?list-all");
		candidateIsShown(result, "exit");
		candidateIsShown(result, MainHandler.SHUTDOWN);
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
		candidateIsNotShown(result, "exit");
		candidateIsNotShown(result, MainHandler.SHUTDOWN);
	}
	
	@Test
	public void commandNameNothingToComplete() throws IOException{
		simulateUserInput("doesNotExists\t");
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
		candidateIsNotShown(result, "exit");
		candidateIsNotShown(result, MainHandler.SHUTDOWN);
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
