package de.raysha.lib.jsimpleshell.it;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.exception.CLIException;
import de.raysha.lib.jsimpleshell.handler.OutputDependent;
import de.raysha.lib.jsimpleshell.handler.impl.AbstractMessageResolver;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;

public class L18nTest {
	public class Commands implements OutputDependent {
		OutputBuilder out;
		
		@Override
		public void cliSetOutput(OutputBuilder output) {
			this.out = output;
		}
		
		@Command(description = "{cmd.desc}")
		public void test(){
			
		}
		
		@Command
		public String message(){
			return "{general.message}";
		}
		
		@Command
		public void output(){
			out.out().normal("{general.message}").println();
		}
	}
	
	public class MyMessageResolver extends AbstractMessageResolver {
		
		public String resolveMessage(String message){
			return message.replace("{cmd.desc}", "Test-Description")
						.replace("{general.message}", "Resolved-General-Message");
		}
	}
	
	SilentShell shell;
	
	@Before
	public void setup() throws IOException{
		ShellBuilder builder = ShellBuilder.shell("jss");
		builder.addHandler(new Commands());
		builder.addAuxHandler(new MyMessageResolver());
		
		shell = new SilentShell(builder);
		shell.start();
	}
	
	@Test
	public void testResolvingCommand() throws IOException {
		shell.executeCommand("?help", "test");
		shell.executeCommand("exit");

		shell.waitForShell();
		
		assertTrue("The desciption was not resolved! " + shell.getOut(),
				shell.getOut().contains("Test-Description"));
	}
	
	@Test
	public void testResolvingReturnValue() throws IOException, CLIException{
		shell.executeCommand("message");
		shell.executeCommand("exit");
		
		shell.waitForShell();

		assertTrue("The return value was not resolved! " + shell.getOut(),
				shell.getOut().contains("Resolved-General-Message"));
	}
	
	@Test
	public void testResolvingOutput() throws IOException, CLIException{
		shell.executeCommand("output");
		shell.executeCommand("exit");
		
		shell.waitForShell();

		assertTrue("The return value was not resolved! " + shell.getOut(),
				shell.getOut().contains("Resolved-General-Message"));
	}
}
