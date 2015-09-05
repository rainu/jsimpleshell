package de.raysha.lib.jsimpleshell;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;

import java.io.IOException;

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

	public static String SPECIAL_EXIT_COMMAND = "quit";

	@Command(startsSubshell = true)
	public void subShellWithSpecialExit() throws IOException{
		ShellBuilder.subshell("sub", shell)
				.behavior()
					.disableExitCommand(SPECIAL_EXIT_COMMAND)
					.addHandler(new ExitAlternativeCommands())
					.addHandler(new VariablePlaygroundCommands())
				.build()
				.commandLoop();
	}

}
