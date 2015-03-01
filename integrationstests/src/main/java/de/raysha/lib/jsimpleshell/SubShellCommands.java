package de.raysha.lib.jsimpleshell;

import java.io.IOException;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;

public class SubShellCommands {
	@Inject private Shell shell;

	@Command(startsSubshell = true)
	public void newSubShell() throws IOException{
		ShellBuilder.subshell("sub", shell)
			.behavior()
				.addHandler(new VariablePlaygroundCommands())
			.build()
		.commandLoop();
	}

	@Command(startsSubshell = true)
	public void subShellWithoutExit() throws IOException{
		ShellBuilder.subshell("sub", shell)
			.behavior()
				.disableExitCommand()
				.addHandler(new ExitAlternativeCommands())
				.addHandler(new VariablePlaygroundCommands())
			.build()
		.commandLoop();
	}
}
