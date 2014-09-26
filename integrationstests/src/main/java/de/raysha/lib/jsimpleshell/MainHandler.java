package de.raysha.lib.jsimpleshell;

import java.io.InterruptedIOException;

import de.raysha.lib.jsimpleshell.annotation.Command;


public class MainHandler {
	public static final String SHUTDOWN = "shutdown";

	@Command(SHUTDOWN)
	public void shutdown() throws InterruptedIOException {
		throw new InterruptedIOException();
	}

	@Command
	public void causeException() throws InterruptedException {
		Thread.sleep(5);

		throw new RuntimeException("Error!");
	}
}
