package de.raysha.lib.jsimpleshell.builder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jline.console.ConsoleReader;

/**
 * This is a part of the {@link ShellBuilder}. It is responsible for configuring
 * all stuff that has to do with the input/output of the shell.
 *
 * @author rainu
 */
public class IO {
	private final BuilderModel model;
	private final ShellBuilder shellBuilder;

	IO(BuilderModel model, ShellBuilder shellBuilder) {
		this.model = model;
		this.shellBuilder = shellBuilder;
	}

	/**
	 * Set the {@link ConsoleReader} which you want to use.
	 *
	 * @param console The {@link ConsoleReader} instance.
	 * @return This {@link IO}
	 */
	public IO setConsole(ConsoleReader console) {
		model.setConsole(console);
		model.setUseForeignConsole(true);

		return this;
	}

	/**
	 * Set Error {@link OutputStream} which will be used for printing error messages.
	 *
	 * @param error The {@link OutputStream} instance.
	 * @return This {@link IO}
	 */
	public IO setError(OutputStream error) {
		model.setError(error);

		return this;
	}

	/**
	 * Create and use a new {@link ConsoleReader} with the specified
	 * Input- and Output- Stream.
	 *
	 * @param in The {@link InputStream} that should be used.
	 * @param out The {@link OutputStream} that should be used.
	 * @return This {@link IO}
	 */
	public IO setConsole(InputStream in, OutputStream out) throws IOException {
		model.setConsole(new ConsoleReader(in, out));
		model.setUseForeignConsole(false);

		return this;
	}

	/**
	 * Go back to the {@link ShellBuilder}.
	 *
	 * @return The {@link ShellBuilder}
	 */
	public ShellBuilder back(){
		return shellBuilder;
	}
}
