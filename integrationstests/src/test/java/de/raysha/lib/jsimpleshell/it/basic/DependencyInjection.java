package de.raysha.lib.jsimpleshell.it.basic;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.DependencyInjectionCommand;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.ShellBuilder;

public class DependencyInjection extends IntegrationsTest {

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
				.addHandler(new DependencyInjectionCommand());
	}

	@Test
	public void checkDependencyInjection() throws IOException{
		CommandResult result = executeAndWaitForCommand("check-dependencies");

		assertFalse(result.containsLine(".*is null!"));
	}
}
