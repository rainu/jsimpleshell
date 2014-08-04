package de.raysha.lib.jsimpleshell.it;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.exception.CLIException;

public class AmbiguousCommands {
	public class Commands {
		@Command
		public String set(Boolean value){
			return "Boolean: " + value;
		}
		
		@Command
		public String set(Integer value){
			return "Integer: " + value;
		}
		
		@Command
		public String set(String value){
			return "String: " + value;
		}
		
		@Command
		public String setVar(String...value){
			return "String[]: " + Arrays.toString(value);
		}
		
		@Command
		public String setVar(Integer...value){
			return "Integer[]: " + Arrays.toString(value);
		}
		
		@Command
		public String setVar(Boolean...value){
			return "Boolean[]: " + Arrays.toString(value);
		}
		
		@Command
		public String complex(Boolean value, String sValue){
			return "complex: " + value + ", " + sValue;
		}
		
		@Command
		public String complex(String sValue, Boolean value){
			return "complex: " + sValue + ", " + value;
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
		assertTrue(shellInterface.getOut().contains("Boolean: true"));
	}
	
	@Test
	public void setInteger() throws IOException, CLIException{
		shellInterface.executeCommand("set", "130810");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();

		assertEquals("", shellInterface.getErr());
		assertTrue(shellInterface.getOut().contains("Integer: 130810"));
	}
	
	@Test
	public void setString() throws IOException, CLIException{
		shellInterface.executeCommand("set", "Rainu");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();
		
		assertEquals("", shellInterface.getErr());
		assertTrue(shellInterface.getOut().contains("String: Rainu"));
	}
	
	@Test
	public void setVarBoolean() throws IOException, CLIException{
		shellInterface.executeCommand("set-var", "true", "false");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();

		assertEquals("", shellInterface.getErr());
		assertTrue(shellInterface.getOut().contains("Boolean[]: [true, false]"));
	}
	
	@Test
	public void setVarInteger() throws IOException, CLIException{
		shellInterface.executeCommand("set-var", "130810", "130490");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();

		assertEquals("", shellInterface.getErr());
		assertTrue(shellInterface.getOut().contains("Integer[]: [130810, 130490]"));
	}
	
	@Test
	public void setVarString() throws IOException, CLIException{
		shellInterface.executeCommand("set-var", "Rainu", "JSS");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();
		
		assertEquals("", shellInterface.getErr());
		assertTrue(shellInterface.getOut().contains("String[]: [Rainu, JSS]"));
	}
	
	@Test
	public void complex() throws IOException, CLIException{
		shellInterface.executeCommand("complex", "String", "false");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();
		
		assertEquals("", shellInterface.getErr());
		assertTrue(shellInterface.getOut().contains("complex: String, false"));
	}
	
	@Test
	public void complexAmbiguous() throws IOException, CLIException{
		shellInterface.executeCommand("complex", "true", "false");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();
		
		assertTrue(shellInterface.getErr().contains("Ambiguous"));
	}
}
