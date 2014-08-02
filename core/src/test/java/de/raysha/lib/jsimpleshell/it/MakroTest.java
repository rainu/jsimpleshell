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

	SilentShell shell;
	
	@Before
	public void setup() throws IOException{
		ShellBuilder builder = ShellBuilder.shell("jss");
		
		shell = new SilentShell(builder);
	}
	
	@Test
	public void recordMakro() throws IOException, CLIException{
		File makro = File.createTempFile("jsimpleshell", ".makro");
		makro.deleteOnExit();
		
		shell.executeCommand("start-record", makro.getAbsolutePath());
		shell.executeCommand("?list");
		shell.executeCommand("?list-all");
		shell.executeCommand("unknownCommand");
		shell.executeCommand("stop-record");
		
		String makroContent = FileUtils.readFileToString(makro);
		String expectedContent = "?list\n?list-all\nunknownCommand\n";
		
		assertTrue("Makro wasn't created correctely! " + makroContent,
				makroContent.endsWith(expectedContent));
	}
}
