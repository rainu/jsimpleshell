package de.raysha.lib.jsimpleshell;

import java.io.IOException;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;

public class SubShellCommands {
	@Inject private Shell shell;

	@Command
	public void newSubShell() throws IOException{
		ShellBuilder.subshell("sub", shell).build().commandLoop();
	}

	@Command
	public void subShellWithoutExit() throws IOException{
		ShellBuilder.subshell("sub", shell)
			.disableExitCommand()
			.addHandler(new ExitAlternativeCommands())
			.build()
		.commandLoop();
	}
}
