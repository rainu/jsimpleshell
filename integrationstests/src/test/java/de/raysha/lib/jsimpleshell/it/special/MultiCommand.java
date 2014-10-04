package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.AmbiguousCommands;
import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.ExitAlternativeCommands;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.SubShellCommands;
import de.raysha.lib.jsimpleshell.annotation.Command;

public class MultiCommand extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
					.addHandler(new ExitAlternativeCommands())
					.addHandler(new AmbiguousCommands())
					.addHandler(new SubShellCommands())
					.addHandler(new ExceptionTestCommand());
	}

	public static class ExceptionTestCommand{
		@Command
		public void exception(){
			throw new RuntimeException();
		}
	}

	@Test
	public void simpleOr() throws IOException{
		CommandResult result = executeAndWaitForCommand("set value1 ; set value2");

		assertTrue(result.containsLine("String: value1"));
		assertTrue(result.containsLine("String: value2"));
	}

	@Test
	public void simpleAnd() throws IOException{
		CommandResult result = executeAndWaitForCommand("set value1 && set value2");

		assertTrue(result.containsLine("String: value1"));
		assertTrue(result.containsLine("String: value2"));
	}

	@Test
	public void firstFailOr() throws IOException{
		CommandResult result = executeAndWaitForCommand("exception ; set value1");

		assertTrue(result.containsLine("String: value1"));
	}

	@Test
	public void firstFailAnd() throws IOException{
		CommandResult result = executeAndWaitForCommand("exception && set value1");

		assertFalse(result.containsLine("String: value1"));
	}

	@Test
	public void orAnd() throws IOException{
		CommandResult result = executeAndWaitForCommand("exception ; set value1 && set value2");

		assertTrue(result.containsLine("String: value1"));
		assertTrue(result.containsLine("String: value2"));
	}

	@Test
	public void orAnd2() throws IOException{
		CommandResult result = executeAndWaitForCommand("set value1 ; exception && set value2");

		assertTrue(result.containsLine("String: value1"));
		assertFalse(result.containsLine("String: value2"));
	}

	@Test
	public void andOr() throws IOException{
		CommandResult result = executeAndWaitForCommand("exception && set value1 ; set value2");

		assertFalse(result.containsLine("String: value1"));
		assertFalse(result.containsLine("String: value2"));
	}

	@Test
	public void exitBeforeOr() throws IOException{
		executeCommand("new-sub-shell");
		CommandResult result = executeAndWaitForCommand("exit ; ?help");

		assertFalse(result.containsLine("JSimpleShell"));
	}

	@Test
	public void exitBeforeAnd() throws IOException{
		executeCommand("new-sub-shell");
		CommandResult result = executeAndWaitForCommand("exit && ?help");

		assertFalse(result.containsLine("JSimpleShell"));
	}

	@Test
	public void emptyAtTheEnd_Or() throws IOException{
		CommandResult result = executeAndWaitForCommand("set value1 ;");

		assertFalse(result.toString(), result.isError());
	}

	@Test
	public void emptyAtTheEnd_And() throws IOException{
		CommandResult result = executeAndWaitForCommand("set value1 &&");

		assertFalse(result.toString(), result.isError());
	}

	@Test
	public void emptyBetween_Or() throws IOException{
		CommandResult result = executeAndWaitForCommand("set value1 ; ; set value2");

		assertTrue(result.containsLine("String: value1"));
		assertTrue(result.containsLine("String: value2"));
	}

	@Test
	public void emptyBetween_And() throws IOException{
		CommandResult result = executeAndWaitForCommand("set value1 && && set value2");

		assertTrue(result.containsLine("String: value1"));
		assertTrue(result.containsLine("String: value2"));
	}

	@Test
	public void invalidBetween_Or() throws IOException{
		CommandResult result = executeAndWaitForCommand("set value1 ; doesNotExists ; set value2");

		assertTrue(result.containsLine("String: value1"));
		assertTrue(result.containsLine("String: value2"));
		assertTrue(result.containsLine("Unknown command: \"doesNotExists\""));
	}

	@Test
	public void invlaidBetween_And() throws IOException{
		CommandResult result = executeAndWaitForCommand("set value1 && doesNotExists && set value2");

		assertTrue(result.containsLine("String: value1"));
		assertFalse(result.containsLine("String: value2"));
		assertTrue(result.containsLine("Unknown command: \"doesNotExists\""));
	}
}
