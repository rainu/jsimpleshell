package de.raysha.lib.jsimpleshell;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.SilentShell.CommandResult;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.exception.CLIException;

public class L18nTest {
	public class Commands {
		@Command(description = "{cmd.desc}")
		public void test(){
			
		}
	}
	
	public class MyMessageResolver {//implements MessageResolver {
		
		public String resolveMessage(String message){
			return message.replace("{cmd.desc}", "Test-Description");
		}
	}
	
	SilentShell shell;
	
	@Before
	public void setup() throws IOException{
		ShellBuilder builder = ShellBuilder.shell("jss");
		builder.addHandler(new Commands());
		builder.addAuxHandler(new MyMessageResolver());
		
		shell = new SilentShell(builder);
	}
	
	@Test
	public void testResolving() throws IOException, CLIException{
		CommandResult result = shell.executeCommand("?help", "test");

		assertTrue("The desciption was not resolved! " + result.getOut(),
				result.getOut().contains("Test-Description"));
	}
}
