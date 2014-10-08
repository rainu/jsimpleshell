package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.DependencyInjectionCommand;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;

public class DependencyInjection extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.behavior()
					.addHandler(new DependencyInjectionCommand())
				.back();
	}

	@Test
	public void checkDependencyInjection() throws IOException{
		CommandResult result = executeAndWaitForCommand("check-dependencies");

		assertFalse(result.containsLine(".*is null!"));
	}
}
