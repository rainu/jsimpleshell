package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.util.FileUtils;

public class Script extends IntegrationsTest {

	@Test
	public void runScript() throws IOException{
		File tmpScript = File.createTempFile("jss", ".script");
		tmpScript.deleteOnExit();
		
		FileUtils.write(tmpScript, "?list\n", true);
		FileUtils.write(tmpScript, "?help\n", true);
		FileUtils.write(tmpScript, "#this is a comment line\n", true);
		FileUtils.write(tmpScript, "\n", true);
		FileUtils.write(tmpScript, "?help ?list\n", true);
		
		executeAndWaitForCommand("?help", "exit");
		CommandResult result = executeAndWaitForCommand("!run-script", tmpScript.getAbsolutePath());
		assertFalse(result.isError());
		
		assertTrue(result.containsOutLine("IT\\> \\?list"));
		assertTrue(result.containsOutLine("IT\\> \\?help"));
		assertTrue(result.containsOutLine("IT\\> \\?help \\?list"));
		assertFalse(result.containsOutLine("this is a comment line"));
	}
	
	@Test
	public void runParametrizedScript() throws IOException{
		File tmpScript = File.createTempFile("jss", ".script");
		tmpScript.deleteOnExit();
		
		FileUtils.write(tmpScript, "echo {value}\n", true);
		FileUtils.write(tmpScript, "?help\n", true);
		FileUtils.write(tmpScript, "#this is a comment line\n", true);
		FileUtils.write(tmpScript, "\n", true);
		FileUtils.write(tmpScript, "?help ?list\n", true);
		FileUtils.write(tmpScript, "echo {value}\n", true);
		
		executeAndWaitForCommand("?help", "exit");
		CommandResult result = executeAndWaitForCommand("!run-script", 
				tmpScript.getAbsolutePath(), "value=Hello World");
		
		assertFalse(result.isError());

		assertTrue(result.containsOutLine("IT\\> echo Hello World"));
		assertFalse(result.containsOutLine("IT\\> echo {value}"));
		assertTrue(result.containsOutLine("IT\\> \\?help ?list"));
		assertTrue(result.containsOutLine("IT\\> \\?help \\?list"));
		assertFalse(result.containsOutLine("this is a comment line"));
	}
}
