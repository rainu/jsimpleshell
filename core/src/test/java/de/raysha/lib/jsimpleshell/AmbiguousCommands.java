package de.raysha.lib.jsimpleshell;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.SilentShell.CommandResult;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.exception.CLIException;

public class AmbiguousCommands {
	public class Commands {
		@Command
		public Boolean set(Boolean value){
			return value;
		}
		
		@Command
		public Integer set(Integer value){
			return value;
		}
		
		@Command
		public String set(String value){
			return value;
		}
	}
	
	
	SilentShell shell;
	
	@Before
	public void setup() throws IOException{
		ShellBuilder builder = ShellBuilder.shell("jss");
		builder.addHandler(new Commands());
		
		shell = new SilentShell(builder);
	}
	
	@Test
	public void setBoolean() throws IOException, CLIException{
		CommandResult result = shell.executeCommand("set", "true");

		assertEquals("", result.getErr());
		assertEquals("true", result.getOut());
	}
	
	@Test
	public void setInteger() throws IOException, CLIException{
		CommandResult result = shell.executeCommand("set", "130810");

		assertEquals("", result.getErr());
		assertEquals("130810", result.getOut());
	}
	
	@Test
	public void setString() throws IOException, CLIException{
		CommandResult result = shell.executeCommand("set", "Rainu");

		assertEquals("", result.getErr());
		assertEquals("Rainu", result.getOut());
	}
}
