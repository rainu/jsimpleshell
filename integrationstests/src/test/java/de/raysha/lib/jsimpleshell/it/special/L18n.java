package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.L18nCommands;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.CLIException;
import de.raysha.lib.jsimpleshell.handler.impl.AbstractMessageResolver;

public class L18n extends IntegrationsTest {
	public class MyMessageResolver extends AbstractMessageResolver {
		
		public String resolveMessage(String message){
			return message.replace("{cmd.desc}", "Test-Description")
						.replace("{general.message}", "Resolved-General-Message");
		}
	}
	
	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
					.addHandler(new L18nCommands())
					.addAuxHandler(new MyMessageResolver());
	}
	
	@Test
	public void testResolvingCommand() throws IOException {
		executeCommand("?help", "test");

		CommandResult result = waitForShell();
		
		assertTrue("The desciption was not resolved! " + result.getOut(),
				result.getOut().contains("Test-Description"));
	}
	
	@Test
	public void testResolvingReturnValue() throws IOException, CLIException{
		executeCommand("message");
		
		CommandResult result = waitForShell();

		assertTrue("The return value was not resolved! " + result.getOut(),
				result.getOut().contains("Resolved-General-Message"));
	}
	
	@Test
	public void testResolvingOutput() throws IOException, CLIException{
		executeCommand("output");
		
		CommandResult result = waitForShell();

		assertTrue("The return value was not resolved! " + result.getOut(),
				result.getOut().contains("Resolved-General-Message"));
	}
}
