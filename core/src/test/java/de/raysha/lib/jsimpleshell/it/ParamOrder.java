package de.raysha.lib.jsimpleshell.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.exception.CLIException;

public class ParamOrder {
	public class Commands {
		@Command
		public String set(
				@Param(name="p1")
				String p1, 
				@Param(name="p2")
				String p2, 
				@Param(name="p3")
				String p3){
			return p1 + ", " + p2 + ", " + p3;
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
	public void order() throws IOException, CLIException{
		shellInterface.executeCommand("set", "--p3", "p3", "--p1", "p1", "--p2", "p2");
		shellInterface.executeCommand("exit");
		
		shellInterface.waitForShell();

		assertEquals("", shellInterface.getErr());
		assertTrue(shellInterface.getOut().contains("p1, p2, p3"));
	}
}
