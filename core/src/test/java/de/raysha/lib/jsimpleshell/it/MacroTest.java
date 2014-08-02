package de.raysha.lib.jsimpleshell.it;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.CLIException;

public class MacroTest {

	SilentShell shellInterface;
	File macroHome;
	
	@Before
	public void setup() throws IOException{
		ShellBuilder builder = ShellBuilder.shell("jss");
		
		shellInterface = new SilentShell(builder);
		macroHome = File.createTempFile("jsimpleshell", ".macro");
		macroHome.delete();
		macroHome.mkdirs();
		shellInterface.shell.setMacroHome(macroHome);
		
		shellInterface.start();
	}
	
	@After
	public void clean() throws IOException{
		FileUtils.deleteDirectory(macroHome);
	}
	
	@Test
	public void recordMakro() throws IOException, CLIException{
		shellInterface.executeCommand("!start-record", "myMacro");
		shellInterface.executeCommand("?list");
		shellInterface.executeCommand("?list-all");
		shellInterface.executeCommand("unknownCommand");
		shellInterface.executeCommand("!stop-record");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();
		
		String makroContent = FileUtils.readFileToString(new File(macroHome, "myMacro"));
		String expectedContent = "?list\n?list-all\nunknownCommand\n";

		assertTrue("Macro wasn't created correctely! " + makroContent,
				makroContent.endsWith(expectedContent));
	}
	
	@Test
	public void flushMakroOnExit() throws IOException, CLIException{
		shellInterface.executeCommand("!start-record", "myMacro");
		shellInterface.executeCommand("?list");
		shellInterface.executeCommand("?list-all");
		shellInterface.executeCommand("unknownCommand");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();
		
		String makroContent = FileUtils.readFileToString(new File(macroHome, "myMacro"));
		String expectedContent = "?list\n?list-all\nunknownCommand\nexit\n";

		assertTrue("Macro wasn't created correctely! " + makroContent,
				makroContent.endsWith(expectedContent));
	}
	
	@Test
	public void multiRecord() throws IOException, CLIException{
		shellInterface.executeCommand("!start-record", "myMacro");
		shellInterface.executeCommand("?list");
		shellInterface.executeCommand("!start-record", "myMacro");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();
		
		assertTrue(shellInterface.getOut().contains("Macro recording is already started!"));
	}
}
