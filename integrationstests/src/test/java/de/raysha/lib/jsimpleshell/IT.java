package de.raysha.lib.jsimpleshell;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;

public abstract class IT {

	protected SilentShell shellInterface;
	
	@Before
	public void setup() throws IOException{
		this.shellInterface = new SilentShell(buildShell());
		this.shellInterface.start();
	}
	
	@After
	public void tearDown() throws IOException{
		executeAndWaitForCommand(MainHandler.SHUTDOWN);
	}
	
	public CommandResult waitForShellCommandExec() {
		return shellInterface.waitForShellCommandExec();
	}

	public void waitForShellExit() {
		shellInterface.waitForShellExit();
	}

	public CommandResult executeAndWaitForCommand(String cmd, String... arguments) throws IOException {
		executeCommand(cmd, arguments);
		return waitForShellCommandExec();
	}
	
	public void executeCommand(String cmd, String... arguments) throws IOException {
		shellInterface.executeCommand(cmd, arguments);
	}

	public void simulateUserInput(String userInput) throws IOException {
		shellInterface.simulateUserInput(userInput);
	}
	
	public boolean isError(CommandResult result) {
		return !result.getErr().isEmpty();
	}

	protected ShellBuilder buildShell() {
		return ShellBuilder.shell("IT")
					.addAuxHandler(new MainHandler());
	}
}
