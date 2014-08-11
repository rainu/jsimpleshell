package de.raysha.lib.jsimpleshell;

import java.io.IOException;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.handler.ShellDependent;

public class SubShellCommands implements ShellDependent {
	private Shell shell;
	
	@Override
	public void cliSetShell(Shell theShell) {
		this.shell = theShell;
	}
	
	@Command
	public void newSubShell() throws IOException{
		ShellBuilder.subshell("sub", shell).build().commandLoop();
	}
}
