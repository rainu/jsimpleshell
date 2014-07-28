package de.raysha.lib.jsimpleshell;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.SilentShell.CommandResult;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.exception.CLIException;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;
import de.raysha.lib.jsimpleshell.io.OutputDependent;

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
	
	SilentShell silentShell;
	
	@Before
	public void setup() throws IOException{
		ShellBuilder builder = ShellBuilder.shell("jss");
		builder.addHandler(new Commands());
		
		silentShell = new SilentShell(builder);
	}
	
	@Test
	public void disableColor() throws IOException, CLIException{
		silentShell.shell.disableColor();
	
		CommandResult result = silentShell.executeCommand("print");
		
		assertEquals("GREEN", result.getOut());
	}
	
	@Test
	public void enableColor() throws IOException, CLIException{
		silentShell.shell.enableColor();
	
		CommandResult result = silentShell.executeCommand("print");
		
		assertEquals("\u001B[32mGREEN\u001B[0m", result.getOut());
	}
}
