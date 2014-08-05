package de.raysha.lib.jsimpleshell;

import java.io.IOException;

public class CommandLoopThread extends Thread {
	
	private final Shell shell;
	
	public CommandLoopThread(Shell shell) {
		this.shell = shell;
		
		setName("Shell-Command-Loop");
	}
	
	@Override
	public void run() {
		try {
			shell.commandLoop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
