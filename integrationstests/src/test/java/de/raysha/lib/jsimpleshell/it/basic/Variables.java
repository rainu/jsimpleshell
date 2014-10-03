package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.SecurityCommands;
import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.SubShellCommands;
import de.raysha.lib.jsimpleshell.VariablePlaygroundCommands;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision;

public class Variables extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.addHandler(new SubShellCommands())
				.addHandler(new SecurityCommands())
				.addHandler(new ExceptionTestCommand())
				.addHandler(new VariablePlaygroundCommands());
	}

	public static class ExceptionTestCommand{
		@Command
		public void exception(){
			throw new RuntimeException();
		}
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
	public void changeGlobalVariableInSubshell() throws IOException{
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

	@Test
	public void specialVariablesAvailable() throws IOException {
		executeCommand("?help");
		CommandResult result = executeAndWaitForCommand(".show-environment");

		assertTrue(result.toString(), result.containsLine("\\?\\?=0"));
		assertTrue(result.toString(), result.containsLine("\\?=.*"));
	}

	@Test
	public void specialVariablesReturnCodeSuccessful() throws IOException {
		executeCommand("?help");
		CommandResult result = executeAndWaitForCommand(".show-variable", "?");

		assertTrue(result.toString(), result.containsLine(".*\\?"));
		assertTrue(result.toString(), result.containsLine(".*global"));
		assertTrue(result.toString(), result.containsLine(".*" + String.class.getName()));

		executeCommand("?help");
		result = executeAndWaitForCommand(".show-variable", "??");

		assertTrue(result.toString(), result.containsLine(".*\\?\\?"));
		assertTrue(result.toString(), result.containsLine(".*global"));
		assertTrue(result.toString(), result.containsLine(".*" + Integer.class.getName()));
		assertTrue(result.toString(), result.containsLine(".*0"));
	}

	@Test
	public void specialVariablesReturnCodeUnsuccessful() throws IOException {
		executeCommand("exception");
		CommandResult result = executeAndWaitForCommand(".show-variable", "?");

		assertTrue(result.toString(), result.containsLine(".*\\?"));
		assertTrue(result.toString(), result.containsLine(".*global"));
		assertTrue(result.toString(), result.containsLine(".*" + RuntimeException.class.getName()));

		executeCommand("exception");
		result = executeAndWaitForCommand(".show-variable", "??");

		assertTrue(result.toString(), result.containsLine(".*\\?\\?"));
		assertTrue(result.toString(), result.containsLine(".*global"));
		assertTrue(result.toString(), result.containsLine(".*" + Integer.class.getName()));
		assertTrue(result.toString(), result.containsLine(".*1"));
	}

	@Test
	public void specialVariablesReturnCodeForbidden() throws IOException {
		executeCommand("do-something");
		CommandResult result = executeAndWaitForCommand(".show-variable", "?");

		assertTrue(result.toString(), result.containsLine(".*\\?"));
		assertTrue(result.toString(), result.containsLine(".*global"));
		assertTrue(result.toString(), result.containsLine(".*" + AccessDecision.class.getName().replace("$", "\\$")));

		executeCommand("do-something");
		result = executeAndWaitForCommand(".show-variable", "??");

		assertTrue(result.toString(), result.containsLine(".*\\?\\?"));
		assertTrue(result.toString(), result.containsLine(".*global"));
		assertTrue(result.toString(), result.containsLine(".*" + Integer.class.getName()));
		assertTrue(result.toString(), result.containsLine(".*2"));
	}

	@Test
	public void transferStringVariable() throws IOException {
		executeCommand(".lvar", "var", "value");
		CommandResult result = executeAndWaitForCommand("set", "$var");

		assertTrue(result.toString(), result.containsLine("String: value"));
	}

	@Test
	public void transferComplexVariable() throws IOException {
		executeCommand("get-integer");
		CommandResult result = executeAndWaitForCommand("set", "$?");

		assertTrue(result.toString(), result.containsLine("Integer: 13"));
	}

	@Test
	public void transferComplexVariableShouldForceStringValue() throws IOException {
		executeCommand("get-integer");
		CommandResult result = executeAndWaitForCommand("set-string", "$?");

		assertTrue(result.toString(), result.containsLine("String: 13"));
	}

	@Test
	public void saveHelpTextTransferItToOtherCommand() throws IOException {
		executeCommand("?help");
		executeCommand(".lvar", "HELP_TEXT", "$?");

		CommandResult result = executeAndWaitForCommand("set-anything", "$HELP_TEXT");

		assertTrue(result.toString(), result.containsLine("Object: ####.*"));
	}
}
