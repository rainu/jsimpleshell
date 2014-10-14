package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.VariablePlaygroundCommands;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.script.cmd.LoopCommandHandler;

public class Loop extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.behavior()
					.addHandler(new VariablePlaygroundCommands())
				.back();
	}

	@Test
	public void normalLoop() throws IOException{
		executeCommand(".loop-count", "5");
		executeCommand("set 13");
		executeCommand(".loop-end");

		CommandResult result = waitForShell();
		assertEquals(result.toString(),
				5, StringUtils.countMatches(result.getOut(), "Integer: 13"));
	}

	@Test
	public void loopVariables() throws IOException{
		executeCommand(".loop-count", "0", "10", "2");
		executeCommand(".show-environment");
		executeCommand(".loop-end");

		CommandResult result = waitForShell();

		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_FROM + "=0"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_UNTIL + "=10"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_STEP + "=2"));

		for(int i=0; i <= 10; i+=2){
			assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNT + "=" + i));
		}
	}

	@Test
	public void innerLoop() throws IOException, InterruptedException{
		executeCommand(".loop-count", "2");
		executeCommand("set outer");
		executeCommand(".loop-count", "2");
		executeCommand("set inner");
		executeCommand(".loop-end");
		executeCommand(".loop-end");
		Thread.sleep(500);

		CommandResult result = waitForShell();

		String output = StringUtils.substringBetween(result.getOut(), "IT/loop> .loop-end", "IT>");
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
	public void innerLoopEnvironment() throws IOException, InterruptedException{
		executeCommand(".loop-count", "3", "5", "4");
		executeCommand(".loop-count", "4", "6", "2");
		executeCommand(".show-environment");
		executeCommand(".loop-end");
		executeCommand(".show-environment");
		executeCommand(".loop-end");
		Thread.sleep(500);

		CommandResult result = waitForShell();

		//the innerloop will override the outerloop
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_FROM + "=4"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_UNTIL + "=6"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_STEP + "=2"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNT + "=4"));

		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_FROM + "=3"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_UNTIL + "=5"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_STEP + "=4"));
		assertTrue(result.containsOutLine(LoopCommandHandler.VARIABLE_NAME_COUNT + "=3"));
	}
}
