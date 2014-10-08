package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Locale;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.L18nCommands;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.CLIException;
import de.raysha.lib.jsimpleshell.handler.impl.AbstractMessageResolver;

public class L18n extends IntegrationsTest {
	public class MyMessageResolver extends AbstractMessageResolver {

		public boolean supportsLocale(Locale locale) {
			return locale.getLanguage().equals(Locale.ENGLISH.getLanguage());
		}

		public String resolveMessage(String message){
			return message.replace("{cmd.desc}", "Test-Description")
						.replace("{general.message}", "Resolved-General-Message");
		}
	}

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
					.behavior()
						.addHandler(new L18nCommands())
						.addAuxHandler(new MyMessageResolver())
					.back();
	}

	@Test
	public void testResolvingCommand() throws IOException {
		executeCommand("!change-locale", "en");
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

	@Test
	public void testSetLocale() throws IOException{
		CommandResult result = executeAndWaitForCommand("!change-locale", "de");

		assertTrue(result.containsOutLine("Die Sprache wurde ge\u00e4ndert."));
	}

	@Test
	public void testSetLocale_notSupported() throws IOException{
		CommandResult result = executeAndWaitForCommand("!change-locale", "fr");

		assertTrue(result.containsOutLine("The given locale is not supported."));
	}
}
