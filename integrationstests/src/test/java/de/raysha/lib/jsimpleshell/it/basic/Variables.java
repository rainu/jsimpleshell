package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.SubShellCommands;

public class Variables extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.addHandler(new SubShellCommands());
	}

	@Test
	public void createLocalVariable() throws IOException{
		executeCommand(".lvar", "l", "value");
		CommandResult result = executeAndWaitForCommand(".show-environment");

		assertTrue(result.toString(), result.containsLine("l\\=value"));

		result = executeAndWaitForCommand(".show-variable", "l");

		assertTrue(result.toString(), result.containsLine(".*l"));
		assertTrue(result.toString(), result.containsLine(".*local"));
		assertTrue(result.toString(), result.containsLine(".*" + String.class.getName()));
		assertTrue(result.toString(), result.containsLine(".*value"));
	}

	@Test
	public void createEmptyLocalVariable() throws IOException{
		executeCommand(".lvar", "l");
		CommandResult result = executeAndWaitForCommand(".show-environment");

		assertTrue(result.toString(), result.containsLine("l\\=null"));

		result = executeAndWaitForCommand(".show-variable", "l");

		assertTrue(result.toString(), result.containsLine(".*l"));
		assertTrue(result.toString(), result.containsLine(".*local"));
		assertTrue(result.toString(), result.containsLine(".*-"));
		assertTrue(result.toString(), result.containsLine(".*null"));
	}

	@Test
	public void createGlobalVariable() throws IOException{
		executeCommand(".gvar", "g", "value");
		CommandResult result = executeAndWaitForCommand(".show-environment");

		assertTrue(result.toString(), result.containsLine("g\\=value"));

		result = executeAndWaitForCommand(".show-variable", "g");

		assertTrue(result.toString(), result.containsLine(".*g"));
		assertTrue(result.toString(), result.containsLine(".*global"));
		assertTrue(result.toString(), result.containsLine(".*" + String.class.getName()));
		assertTrue(result.toString(), result.containsLine(".*value"));
	}

	@Test
	public void createEmptyGlobalVariable() throws IOException{
		executeCommand(".gvar", "g");
		CommandResult result = executeAndWaitForCommand(".show-environment");

		assertTrue(result.toString(), result.containsLine("g\\=null"));

		result = executeAndWaitForCommand(".show-variable", "g");

		assertTrue(result.toString(), result.containsLine(".*g"));
		assertTrue(result.toString(), result.containsLine(".*global"));
		assertTrue(result.toString(), result.containsLine(".*-"));
		assertTrue(result.toString(), result.containsLine(".*null"));
	}

	@Test
	public void doNotShareLocalVariableWithSubShell() throws IOException{
		executeCommand(".lvar", "l", "value");
		executeCommand("new-sub-shell");

		CommandResult result = executeAndWaitForCommand(".show-environment");

		//the local variable should not be shared with the subshell
		assertFalse(result.toString(), result.containsLine("g\\=value"));
	}

	@Test
	public void doNotShareLocalVariableWithParentShell() throws IOException{
		executeCommand("new-sub-shell");
		executeCommand(".lvar", "l", "value");
		executeCommand("exit");

		CommandResult result = executeAndWaitForCommand(".show-environment");

		//the local variable should not be shared with the parent shell
		assertFalse(result.toString(), result.containsLine("g\\=value"));
	}

	@Test
	public void shareGlobalVariableWithSubShell() throws IOException{
		executeCommand(".gvar", "g", "value");
		executeCommand("new-sub-shell");

		CommandResult result = executeAndWaitForCommand(".show-environment");

		//the global variable should be shared with the subshell
		assertTrue(result.toString(), result.containsLine("g\\=value"));
	}

	@Test
	public void shareGlobalVariableWithParentShell() throws IOException{
		executeCommand("new-sub-shell");
		executeCommand(".gvar", "g", "value");
		executeCommand("exit");

		CommandResult result = executeAndWaitForCommand(".show-environment");

		//the global variable should be shared with the parent shell
		assertTrue(result.toString(), result.containsLine("g\\=value"));
	}

	@Test
	public void changeGlobalVariableInSubshell()throws IOException{
		executeCommand(".gvar", "g");
		executeCommand("new-sub-shell");
		executeCommand(".gvar", "g", "value");

		CommandResult result = executeAndWaitForCommand(".show-environment");
		assertTrue(result.toString(), result.containsLine("g\\=value"));

		executeCommand("exit");
		result = executeAndWaitForCommand(".show-environment");

		//the changes should effect the parent shell too
		assertTrue(result.toString(), result.containsLine("g\\=value"));
	}
}
