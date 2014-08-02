package de.raysha.lib.jsimpleshell.it;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.CLIException;

public class MakroTest {

	SilentShell shellInterface;
	
	@Before
	public void setup() throws IOException{
		ShellBuilder builder = ShellBuilder.shell("jss");
		
		shellInterface = new SilentShell(builder);
	}
	
	@Test
	public void recordMakro() throws IOException, CLIException{
		File macroHome = File.createTempFile("jsimpleshell", ".macro");
		try{
			macroHome.delete();
			macroHome.mkdirs();

			shellInterface.shell.setMacroHome(macroHome);
			shellInterface.start();
			
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
		}finally{
			FileUtils.deleteDirectory(macroHome);
		}
	}
}
