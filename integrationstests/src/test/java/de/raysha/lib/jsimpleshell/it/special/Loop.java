package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.SubShellCommands;
import de.raysha.lib.jsimpleshell.VariablePlaygroundCommands;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.script.cmd.LoopCommandHandler;

public class Loop extends IntegrationsTest {

	@Inject
	private Shell shell;

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.behavior()
					.addHandler(new VariablePlaygroundCommands())
					.addHandler(new SubShellCommands())
					.addHandler(this)
				.back();
	}

	@Test
	public void normalLoop() throws IOException{
		executeCommand(".for", "5");
		executeCommand("set 13");
		executeCommand(".for-end");

		CommandResult result = waitForShell();
		assertEquals(result.toString(),
				5, StringUtils.countMatches(result.getOut(), "Integer: 13"));
	}

	@Test
	public void loopVariables() throws IOException{
		executeCommand(".for", "0", "10", "2");
		executeCommand(".show-environment");
		executeCommand("set $" + LoopCommandHandler.VARIABLE_NAME_COUNTER);
		executeCommand(".for-end");

		CommandResult result = waitForShell();

		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_FROM + "=0"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_UNTIL + "=10"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_STEP + "=2"));

		for(int i=0; i <= 10; i+=2){
			assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=" + i));
			assertTrue(result.containsOutLine("String: " + i));
		}
	}

	@Test
	public void innerLoop() throws IOException, InterruptedException{
		executeCommand(".for", "2");
		executeCommand("set outer");
		executeCommand(".for", "3");
		executeCommand("set inner");
		executeCommand(".for-end");
		executeCommand(".for-end");
		Thread.sleep(500);

		CommandResult result = waitForShell();

		String output = StringUtils.substringBetween(result.getOut(), "IT/loop> .for-end", "IT>");
		assertEquals(
				"String: outer\n" +
				"String: inner\n" +
				"String: inner\n" +
				"String: inner\n" +
				"String: outer\n" +
				"String: inner\n" +
				"String: inner\n" +
				"String: inner",
				output.trim());
	}

	@Test
	public void innerLoopEnvironment() throws IOException, InterruptedException{
		executeCommand(".for", "3", "5", "4");
		executeCommand(".for", "4", "6", "2");
		executeCommand(".show-environment");
		executeCommand(".for-end");
		executeCommand(".show-environment");
		executeCommand(".for-end");
		Thread.sleep(500);

		CommandResult result = waitForShell();

		//the innerloop will override the outerloop
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_FROM + "=4"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_UNTIL + "=6"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_STEP + "=2"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=4"));

		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_FROM + "=3"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_UNTIL + "=5"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_STEP + "=4"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=3"));
	}

	@Test
	public void foreach() throws IOException{
		executeCommand(".foreach", "1", "2", "3", "4", "5");
		executeCommand("set 13");
		executeCommand(".for-end");

		CommandResult result = waitForShell();
		assertEquals(result.toString(),
				5, StringUtils.countMatches(result.getOut(), "Integer: 13"));
	}

	@Test
	public void foreachVariables() throws IOException{
		executeCommand(".foreach", "1", "2", "3");
		executeCommand(".show-environment");
		executeCommand("set $" + LoopCommandHandler.VARIABLE_NAME_COUNTER);
		executeCommand(".for-end");

		CommandResult result = waitForShell();

		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=1"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=2"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=3"));

		assertTrue(result.containsOutLine("String: 1"));
		assertTrue(result.containsOutLine("String: 2"));
		assertTrue(result.containsOutLine("String: 3"));
	}

	@Test
	public void innerForeach() throws IOException, InterruptedException{
		executeCommand(".foreach", "1", "2");
		executeCommand("set outer");
		executeCommand(".foreach", "3", "4");
		executeCommand("set inner");
		executeCommand(".fe");
		executeCommand(".fe");
		Thread.sleep(500);

		CommandResult result = waitForShell();

		String output = StringUtils.substringBetween(result.getOut(), "IT/loop> .fe", "IT>");
		assertEquals(
				"String: outer\n" +
				"String: inner\n" +
				"String: inner\n" +
				"String: outer\n" +
				"String: inner\n" +
				"String: inner",
				output.trim());
	}

	@Test
	public void innerForeachEnvironment() throws IOException, InterruptedException{
		executeCommand(".foreach", "1", "2");
		executeCommand(".show-environment");
		executeCommand(".foreach", "3", "4");
		executeCommand(".show-environment");
		executeCommand(".for-end");
		executeCommand(".for-end");
		Thread.sleep(500);

		CommandResult result = waitForShell();

		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=1"));
		assertEquals(2, StringUtils.countMatches(result.getOut(), LoopCommandHandler.VARIABLE_NAME_COUNTER + "=3"));
		assertEquals(2, StringUtils.countMatches(result.getOut(), LoopCommandHandler.VARIABLE_NAME_COUNTER + "=4"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=2"));
	}

	@Test
	public void foreachIterable() throws IOException{
		shell.getEnvironment().setVariable("iter", Arrays.asList("1", "2", "3"));

		executeCommand(".foreach", "$iter");
		executeCommand(".show-environment");
		executeCommand(".for-end");

		CommandResult result = waitForShell();

		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=1"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=2"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=3"));
	}

	@Test
	public void foreachIterator() throws IOException{
		shell.getEnvironment().setVariable("iter", Arrays.asList("1", "2", "3").iterator());

		executeCommand(".foreach", "$iter");
		executeCommand(".show-environment");
		executeCommand(".for-end");

		CommandResult result = waitForShell();

		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=1"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=2"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=3"));
	}

	@Test
	public void foreachMixed() throws IOException{
		shell.getEnvironment().setVariable("i", 1);
		shell.getEnvironment().setVariable("j", 2L);
		shell.getEnvironment().setVariable("k", "3");

		executeCommand(".foreach", "$i", "$j", "$k");
		executeCommand(".show-environment");
		executeCommand(".for-end");

		CommandResult result = waitForShell();

		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=1"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=2"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNTER + "=3"));
	}

	@Test
	public void foreachWithVariablesInSubshellWithoutExit() throws IOException{
		executeCommand(".foreach 1 2 3");
		executeCommand("sub-shell-without-exit");
		executeCommand("set $" + LoopCommandHandler.VARIABLE_NAME_COUNTER);
		executeCommand("quit");
		executeCommand(".for-end");

		CommandResult result = waitForShell();

		assertTrue(result.containsOutLine("String: 1"));
		assertTrue(result.containsOutLine("String: 2"));
		assertTrue(result.containsOutLine("String: 3"));
	}

	@Test
	public void foreachWithVariablesInSubshell() throws IOException{
		executeCommand(".foreach 1 2 3");
		executeCommand("new-sub-shell");
		executeCommand("set $" + LoopCommandHandler.VARIABLE_NAME_COUNTER);
		executeCommand("exit");
		executeCommand(".for-end");

		CommandResult result = waitForShell();

		assertTrue(result.containsOutLine("String: 1"));
		assertTrue(result.containsOutLine("String: 2"));
		assertTrue(result.containsOutLine("String: 3"));
	}

	@Test
	public void autoCompleteVariables() throws IOException{
		executeCommand(".for", "5");
		simulateUserInput("set $\t");
		CommandResult result = waitForShellCommandExec();

		assertFalse(result.toString(), result.isError());
		assertTrue(result.getOut().contains("$" + LoopCommandHandler.VARIABLE_NAME_COUNTER));
		assertTrue(result.getOut().contains("$" + LoopCommandHandler.VARIABLE_NAME_FROM));
		assertTrue(result.getOut().contains("$" + LoopCommandHandler.VARIABLE_NAME_UNTIL));
		assertTrue(result.getOut().contains("$" + LoopCommandHandler.VARIABLE_NAME_STEP));
	}
}
