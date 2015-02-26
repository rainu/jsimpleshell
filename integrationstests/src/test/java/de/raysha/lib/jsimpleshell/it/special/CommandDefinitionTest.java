package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Locale;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.CommandsWithoutAnnotation;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.SubShellCommands;
import de.raysha.lib.jsimpleshell.annotation.CommandDefinition;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.handler.ShellDependent;
import de.raysha.lib.jsimpleshell.handler.impl.AbstractMessageResolver;

public class CommandDefinitionTest extends IntegrationsTest implements ShellDependent {

	public static class MyMessageResolver extends AbstractMessageResolver {
		@Override
		protected String resolveMessage(String msg) {
			if(msg.startsWith("dolly") && !msg.endsWith("_resolved")){
				return msg + "_resolved";
			}

			return msg;
		}

		@Override
		public boolean supportsLocale(Locale locale) {
			return true;
		}
	}

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.behavior()
					.addHandler(this)
					.addHandler(new MyMessageResolver())
					.addHandler(new SubShellCommands())
				.back();
	}

	@Override
	public void cliSetShell(Shell theShell) {
		CommandsWithoutAnnotation handler = new CommandsWithoutAnnotation();
		CommandDefinition def;
		try {
			def = new CommandDefinition(handler, handler.getClass().getMethod("hello"));
		} catch (Exception e) {
			throw new IllegalStateException("Could not get method!", e);
		}

		theShell.addMainCommand(def);

		//multiple def
		def = new CommandDefinition(
				def.getHandler(), def.getMethod(),
				def.getPrefix(), "dolly.name",
				"dolly.abbrev", "dolly.desc", "dolly.header",
				def.getDisplayResult(), def.getStartsSubshell());

		theShell.addMainCommand(def);

		//aux def
		try {
			def = new CommandDefinition(
					handler, handler.getClass().getMethod("hello"),
					"aux.name", "aux.abbrev");

			theShell.addAuxCommand(def);
		} catch (Exception e) {
			throw new IllegalStateException("Could not get method!", e);
		}

	}

	@Test
	public void isListed() throws IOException{
		CommandResult result = executeAndWaitForCommand("?list");

		assertTrue(result.toString(), result.containsOutLine(".*hello.*"));
		assertTrue(result.toString(), result.containsOutLine(".*dolly.name_resolved.*"));
		assertTrue(result.toString(), result.containsOutLine(".*dolly.abbrev_resolved.*"));
		assertTrue(result.toString(), result.containsOutLine(".*aux.name.*"));
		assertTrue(result.toString(), result.containsOutLine(".*aux.abbrev.*"));

		executeCommand("new-sub-shell");

		result = executeAndWaitForCommand("?list");

		assertFalse(result.toString(), result.containsOutLine(".*hello.*"));
		assertFalse(result.toString(), result.containsOutLine(".*dolly.name_resolved.*"));
		assertFalse(result.toString(), result.containsOutLine(".*dolly.abbrev_resolved.*"));
		assertTrue(result.toString(), result.containsOutLine(".*aux.name.*"));
		assertTrue(result.toString(), result.containsOutLine(".*aux.abbrev.*"));
	}

	@Test
	public void execute() throws IOException{
		CommandResult result = executeAndWaitForCommand("hello");

		assertTrue(result.toString(), result.containsOutLine("^Hello World$"));

		executeCommand("new-sub-shell");

		result = executeAndWaitForCommand("aux.name");
		assertTrue(result.toString(), result.containsOutLine("^Hello World$"));
	}

	@Test
	public void executeDolly() throws IOException{
		CommandResult result = executeAndWaitForCommand("dolly.name_resolved");

		assertTrue(result.toString(), result.containsOutLine("^hello$"));
		assertTrue(result.toString(), result.containsOutLine(".*dolly.header_resolved.*"));
	}

	@Test
	public void helpText() throws IOException{
		CommandResult result = executeAndWaitForCommand("?help dolly.name_resolved");

		assertTrue(result.toString(), result.containsOutLine(".*dolly.name_resolved.*"));
		assertTrue(result.toString(), result.containsOutLine(".*dolly.abbrev_resolved.*"));
		assertTrue(result.toString(), result.containsOutLine(".*dolly.desc_resolved.*"));
	}
}
