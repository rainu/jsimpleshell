package de.raysha.lib.jsimpleshell.it;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.exception.CLIException;
import de.raysha.lib.jsimpleshell.handler.OutputDependent;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;

public class ColorTest {
	public class Commands implements OutputDependent{
		private OutputBuilder output;
		
		@Override
		public void cliSetOutput(OutputBuilder output) {
			this.output = output;
		}
		
		@Command
		public void print(){
			output.out().green("GREEN").print();
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
	public void disableColor() throws IOException, CLIException{
		shellInterface.shell.disableColor();
	
		shellInterface.executeCommand("print");
		shellInterface.executeCommand("exit");
		shellInterface.waitForShell();
		
		assertTrue(shellInterface.getOut().contains("GREEN"));
		assertFalse(shellInterface.getOut().contains("\u001B[32mGREEN\u001B[0m"));
	}
	
	@Test
	public void enableColor() throws IOException, CLIException{
		shellInterface.shell.enableColor();
	
		shellInterface.executeCommand("print");
		shellInterface.executeCommand("exit");
		shellInterface.waitForShell();
		
		assertTrue(shellInterface.getOut().contains("\u001B[32mGREEN\u001B[0m"));
	}
}
