package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.PromptElement;
import de.raysha.lib.jsimpleshell.ShellBuilder;

public class Prompt extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.setPrompt(new PromptElement() {
					@Override
					public String render() {
						return System.getProperty("user.name");
					}
				});
	}

	@Test
	public void specialPrompt() throws IOException{
		CommandResult result = waitForShell();

		assertTrue(result.containsOutLine(System.getProperty("user.name") + ".*"));
	}
}
