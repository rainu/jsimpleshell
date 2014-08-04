package de.raysha.lib.jsimpleshell.it;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.ShellBuilder;
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
	
	
	SilentShell shellInterface;
	
	@Before
	public void setup() throws IOException{
		ShellBuilder builder = ShellBuilder.shell("jss");
		builder.addHandler(new Commands());
		
		shellInterface = new SilentShell(builder);
		shellInterface.start();
	}
	
	@Test
	public void setBoolean() throws IOException, CLIException{
		shellInterface.executeCommand("set", "true");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();

		assertEquals("", shellInterface.getErr());
		assertEquals("true", shellInterface.getOut());
	}
	
	@Test
	public void setInteger() throws IOException, CLIException{
		shellInterface.executeCommand("set", "130810");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();

		assertEquals("", shellInterface.getErr());
		assertEquals("130810", shellInterface.getOut());
	}
	
	@Test
	public void setString() throws IOException, CLIException{
		shellInterface.executeCommand("set", "Rainu");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();
		
		assertEquals("", shellInterface.getErr());
		assertEquals("Rainu", shellInterface.getOut());
	}
}
