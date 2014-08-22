package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.CompleterCommands;
import de.raysha.lib.jsimpleshell.CompleterCommands.TestEnum;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.MainHandler;
import de.raysha.lib.jsimpleshell.ParamOrderCommands;
import de.raysha.lib.jsimpleshell.ShellBuilder;

public class AutoComplete extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
					.addHandler(new CompleterCommands())
					.addHandler(new ParamOrderCommands());
	}

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

	@Test
	public void commandNameArgumentCompleter() throws IOException{
		simulateUserInput("?help \t");
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
	public void fileCompleter() throws IOException{
		simulateUserInput("file \t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsShown(result, "pom.xml");
		candidateIsShown(result, "src");
	}

	@Test
	public void fileCompleter2() throws IOException{
		simulateUserInput("file p\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsShown(result, "pom.xml");
	}

	@Test
	public void dirCompleter() throws IOException{
		simulateUserInput("dir \t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsNotShown(result, "pom.xml");
		candidateIsShown(result, "src");
	}

	@Test
	public void dirCompleter2() throws IOException{
		simulateUserInput("dir s\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsNotShown(result, "pom.xml");
		candidateIsShown(result, "src/");
	}

	@Test
	public void booleanCompleter() throws IOException{
		simulateUserInput("boolean \t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsShown(result, "true");
		candidateIsShown(result, "false");
	}

	@Test
	public void booleanCompleter2() throws IOException{
		simulateUserInput("boolean t\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsShown(result, "true");
		candidateIsNotShown(result, "false");
	}

	@Test
	public void booleanCompleter3() throws IOException{
		simulateUserInput("boolean f\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsNotShown(result, "true");
		candidateIsShown(result, "false");
	}

	@Test
	public void booleanCompleter4() throws IOException{
		simulateUserInput("boolean Tr\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsShown(result, "true");
		candidateIsNotShown(result, "false");
	}

	@Test
	public void booleanCompleter5() throws IOException{
		simulateUserInput("boolean Fa\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsNotShown(result, "true");
		candidateIsShown(result, "false");
	}

	@Test
	public void enumCompleter() throws IOException{
		simulateUserInput("enum \t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		for(TestEnum e : TestEnum.values()){
			candidateIsShown(result, e.name());
		}
	}

	@Test
	public void enumCompleter2() throws IOException{
		simulateUserInput("enum a\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		for(TestEnum e : TestEnum.values()){
			candidateIsShown(result, e.name());
		}
	}

	@Test
	public void macroCompleter() throws IOException{
		executeCommand("!start-record", "TestMacro");
		executeCommand("!stop-record");
		executeCommand("!start-record", "TestMacro1");
		executeCommand("!stop-record");
		waitForShellCommandExec();

		simulateUserInput("!run-macro \t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsShown(result, "TestMacro");
		candidateIsShown(result, "TestMacro1");
	}

	@Test
	public void customCompleter() throws IOException{
		simulateUserInput("custom \t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsShown(result, "Candidate1");
		candidateIsShown(result, "Candidate2");
	}

	@Test
	public void customCompleter2() throws IOException{
		simulateUserInput("custom C\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsShown(result, "Candidate1");
		candidateIsShown(result, "Candidate2");
	}

	@Test
	public void parameterNameCompleter_doesntTrigger() throws IOException{
		simulateUserInput("set -\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsNotShown(result, "--p1");
		candidateIsNotShown(result, "--p2");
		candidateIsNotShown(result, "--p-3");

		simulateUserInput("set \t");
		result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsNotShown(result, "--p1");
		candidateIsNotShown(result, "--p2");
		candidateIsNotShown(result, "--p-3");
	}

	@Test
	public void parameterNameCompleter() throws IOException{
		simulateUserInput("set --\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsShown(result, "--p1");
		candidateIsShown(result, "--p2");
		candidateIsShown(result, "--p-3");
	}

	@Test
	public void parameterNameCompleter_noTrailingParameterName() throws IOException{
		simulateUserInput("set --p1 --\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsNotShown(result, "--p2");
		candidateIsNotShown(result, "--p-3");
	}

	@Test
	public void parameterNameCompleter_argumentType() throws IOException{
		simulateUserInput("set --p2 \t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsShown(result, "true");
		candidateIsShown(result, "false");
	}

	@Test
	public void parameterNameCompleter_multipleComands() throws IOException{
		simulateUserInput("set --opt o1 --\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsShown(result, "--p1");
		candidateIsNotShown(result, "--p2");
		candidateIsNotShown(result, "--p-3");
	}

	@Test
	public void parameterNameCompleter_varArgs() throws IOException{
		simulateUserInput("set --p1 p1 --opt op1 --\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.isError());

		candidateIsShown(result, "--opt");
		assertTrue(result.toString(), result.containsLine("^--opt$"));
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
