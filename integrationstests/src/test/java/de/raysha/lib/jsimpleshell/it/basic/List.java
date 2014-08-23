package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.MainHandler;

public class List extends IntegrationsTest {

	@Test
	public void list() throws IOException {
		final CommandResult result = executeAndWaitForCommand("?list");

		assertFalse(result.isError());
		isCommandListed(result, "", "s", MainHandler.SHUTDOWN);
		isCommandListed(result, "", "exit", "exit");
	}

	@Test
	public void listAll() throws IOException {
		final CommandResult result = executeAndWaitForCommand("?list-all");

		assertFalse(result.isError());
		isCommandListed(result, "", "s", MainHandler.SHUTDOWN);
		isCommandListed(result, "", "exit", "exit");
		isCommandListed(result, "!", "smh", "set-macro-home", "path");
		isCommandListed(result, "!", "rs", "run-script", "filename", "script-argument...");
		isCommandListed(result, "!", "lsa", "list-script-arguments", "filename");
		isCommandListed(result, "!", "gmh", "get-macro-home");
		isCommandListed(result, "!", "rm", "run-macro", "macroName");
		isCommandListed(result, "!", "sr", "start-record", "macroName");
		isCommandListed(result, "!", "str", "stop-record");
		isCommandListed(result, "!", "gle", "get-last-exception");
		isCommandListed(result, "!", "sdt", "set-display-time", "do-display-time");
		isCommandListed(result, "?", "l", "list", "startsWith");
		isCommandListed(result, "?", "l", "list");
		isCommandListed(result, "?", "h", "help", "command-name");
		isCommandListed(result, "?", "h", "help");
		isCommandListed(result, "?", "la", "list-all");
		isCommandListed(result, "?", "ghh", "generate-HTML-help", "file-name", "include-prefixed");
	}

	@Test
	public void listStartWith() throws IOException {
		final CommandResult result = executeAndWaitForCommand("?list", "!set");

		assertFalse(result.isError());
		isCommandNotListed(result, "", "s", MainHandler.SHUTDOWN);
		isCommandNotListed(result, "", "exit", "exit");
		isCommandListed(result, "!", "smh", "set-macro-home", "path");
		isCommandNotListed(result, "!", "rs", "run-script", "filename");
		isCommandNotListed(result, "!", "gmh", "get-macro-home");
		isCommandNotListed(result, "!", "rm", "run-macro", "macroName");
		isCommandNotListed(result, "!", "sr", "start-record", "macroName");
		isCommandNotListed(result, "!", "str", "stop-record");
		isCommandNotListed(result, "!", "gle", "get-last-exception");
		isCommandListed(result, "!", "sdt", "set-display-time", "do-display-time");
		isCommandNotListed(result, "?", "l", "list", "startsWith");
		isCommandNotListed(result, "?", "l", "list");
		isCommandNotListed(result, "?", "h", "help", "command-name");
		isCommandNotListed(result, "?", "h", "help");
		isCommandNotListed(result, "?", "la", "list-all");
		isCommandNotListed(result, "?", "ghh", "generate-HTML-help", "file-name", "include-prefixed");
	}

	private void isCommandListed(final CommandResult result, String prefix, String abbrev, String name, String...parameters) {
		String line = buildRegexp(prefix, abbrev, name, parameters);
		assertTrue("The command '" + prefix + name + "(" + Arrays.toString(parameters) + ")' is not listed!",
				result.containsOutLine(line));
	}

	private void isCommandNotListed(final CommandResult result, String prefix, String abbrev, String name, String...parameters) {
		String line = buildRegexp(prefix, abbrev, name, parameters);
		assertFalse("The command '" + prefix + name + "(" + Arrays.toString(parameters) + ")' is listed!",
				result.containsOutLine(line));
	}

	private String buildRegexp(String prefix, String abbrev, String name, String... parameters) {
		StringBuilder line = new StringBuilder();
		line.append(prefix == null || prefix.isEmpty() ? "" : "\\" + prefix);
		line.append(abbrev);
		line.append("\\s*");
		line.append(prefix == null || prefix.isEmpty() ? "" : "\\" + prefix);
		line.append(name);
		line.append("\\s*\\(");

		for(int i=0; i < parameters.length; i++){
			String p = parameters[i];

			if(i >= 1) {
				line.append("\\s*,\\s*");
			}

			line.append(p);
		}

		line.append("\\)");
		return line.toString();
	}
}
