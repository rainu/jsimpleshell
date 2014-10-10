package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.io.IOException;

import org.apache.commons.lang.SystemUtils;
import org.junit.Before;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.VariablePlaygroundCommands;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;

public class ExecutionCommand extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.behavior()
					.enableProcessStarterCommands()
					.addHandler(new VariablePlaygroundCommands())
				.back();
	}

	@Before
	public void before() {
		//This tests can only run under the unix os.
		assumeTrue(supportsOS());
	}

	private boolean supportsOS() {
		return SystemUtils.IS_OS_UNIX;
	}

	@Test
	public void output() throws IOException{
		executeCommand(".exec", "echo" , "Execution-Output");
		CommandResult result = waitForShellCommandExec();

		assertTrue(result.toString(), result.containsOutLine("^Execution-Output$"));
	}

	@Test
	public void error() throws IOException, InterruptedException{
		executeCommand(".exec", "expr", "1", "+", "a");
		Thread.sleep(1500);

		CommandResult result = waitForShell();
		assertTrue(result.toString(), result.containsErrLine("^expr: non-integer argument$"));
	}

	@Test
	public void buildSimple() throws IOException{
		executeCommand(".exec");
		executeCommand("set-command", "echo");
		executeCommand("set-arguments", "Execution-Output");

		CommandResult result = executeAndWaitForCommand("run");

		assertTrue(result.toString(), result.containsOutLine("^Execution-Output$"));
	}

	@Test
	public void buildCancel() throws IOException{
		executeCommand(".exec");
		executeCommand("set-command", "echo");
		executeCommand("set-arguments", "Execution-Output");

		CommandResult result = executeAndWaitForCommand("cancel");

		assertFalse(result.toString(), result.containsLine("^Execution-Output$"));
	}

	@Test
	public void buildAdvanced() throws IOException{
		executeCommand(".exec");
		executeCommand("set-command", "env");
		executeCommand("set-environment", "env1", "value1");
		executeCommand("set-environment", "env2", "value2");
		executeCommand("set-environment", "env3", "value3");
		executeCommand("remove-environment", "env2");

		CommandResult result = executeAndWaitForCommand("run");

		assertTrue(result.toString(), result.containsOutLine("^env1=value1$"));
		assertTrue(result.toString(), result.containsOutLine("^env3=value3$"));
	}

	@Test
	public void buildWorkingDirectory() throws IOException{
		executeAndWaitForCommand(".exec");
		executeAndWaitForCommand("set-command", "pwd");
		executeAndWaitForCommand("set-home", SystemUtils.JAVA_IO_TMPDIR);

		CommandResult result = executeAndWaitForCommand("run");

		assertTrue(result.toString(), result.containsOutLine(SystemUtils.JAVA_IO_TMPDIR));
	}

	@Test
	public void buildShow() throws IOException{
		executeAndWaitForCommand(".exec");
		executeAndWaitForCommand("set-command", "env");
		executeAndWaitForCommand("set-arguments", "arg1", "arg2");
		executeAndWaitForCommand("set-environment", "env1", "value1");
		executeAndWaitForCommand("set-environment", "env2", "value2");
		executeAndWaitForCommand("set-environment", "env3", "value3");
		executeAndWaitForCommand("disable-output");
		executeAndWaitForCommand("enable-output");
		executeAndWaitForCommand("disable-error");
		executeAndWaitForCommand("enable-error");
		executeAndWaitForCommand("disable-error");
		executeAndWaitForCommand("set-home", "/tmp");
		executeAndWaitForCommand("remove-environment", "env2");

		CommandResult result = executeAndWaitForCommand("show");

		assertTrue(result.toString(), result.containsOutLine("\\s*env1=value1$"));
		assertFalse(result.toString(), result.containsOutLine("\\s*env2=value2$"));
		assertTrue(result.toString(), result.containsOutLine("\\s*env3=value3$"));
		assertTrue(result.toString(), result.containsOutLine("^Command line: env \"arg1\" \"arg2\".*$"));
		assertTrue(result.toString(), result.containsOutLine("^Working directory: /tmp$"));
		assertTrue(result.toString(), result.containsOutLine("Default-Output: enable$"));
		assertTrue(result.toString(), result.containsOutLine("Error-Output: disable$"));
	}

	@Test
	public void invisibleOutput() throws IOException{
		executeAndWaitForCommand(".exec");
		executeAndWaitForCommand("set-command", "echo");
		executeAndWaitForCommand("set-arguments", "Execution-Output");
		executeAndWaitForCommand("disable-output");

		CommandResult result = executeAndWaitForCommand("run");

		assertFalse(result.toString(), result.containsOutLine("^Execution-Output$"));
	}

	@Test
	public void invisibleError() throws IOException, InterruptedException{
		executeAndWaitForCommand(".exec");
		executeAndWaitForCommand("set-command", "expr");
		executeAndWaitForCommand("set-arguments", "1", "+", "a");
		executeAndWaitForCommand("disable-error");
		executeCommand("run");
		Thread.sleep(1500);

		CommandResult result = waitForShell();

		assertFalse(result.toString(), result.containsErrLine("^expr: non-integer argument$"));
	}

	@Test
	public void variableStringResolving() throws IOException{
		executeAndWaitForCommand(".exec", "echo", "Execution-Output");
		CommandResult result = executeAndWaitForCommand("set", "$?");

		assertTrue(result.toString(), result.containsOutLine("^String: Execution-Output$"));
	}

	@Test
	public void variableResolving() throws IOException{
		executeAndWaitForCommand(".exec", "echo", "Execution-Output");
		CommandResult result = executeAndWaitForCommand("set-anything", "$?");

		assertTrue(result.toString(), result.containsOutLine("^Object: .*ProcessResult.*$"));
	}
}
