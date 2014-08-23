package de.raysha.lib.jsimpleshell;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;

public abstract class IntegrationsTest {

	protected SilentShell shellInterface;

	@Before
	public void setup() throws IOException{
		Locale.setDefault(Locale.ENGLISH);

		this.shellInterface = new SilentShell(buildShell());
		this.shellInterface.start();

		this.shellInterface.getShell().changeLocale(Locale.ENGLISH);
	}

	@After
	public void tearDown() throws IOException{
		executeAndWaitForCommand(MainHandler.SHUTDOWN);
	}

	public CommandResult waitForShellCommandExec() {
		return shellInterface.waitForShellCommandExec();
	}

	public CommandResult waitForShell() {
		return shellInterface.waitForShell();
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

	protected ShellBuilder buildShell() throws IOException {
		File macroHome = File.createTempFile("jss", ".dir");
		macroHome.delete(); macroHome.mkdirs();
		macroHome.deleteOnExit();

		File historyFile = File.createTempFile("jss", ".history");
		historyFile.deleteOnExit();

		return ShellBuilder.shell("IT")
					.addAuxHandler(new MainHandler())
					.setMacroHome(macroHome)
					.setHistoryFile(historyFile);
	}
}
