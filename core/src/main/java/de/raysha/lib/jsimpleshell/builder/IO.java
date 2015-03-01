package de.raysha.lib.jsimpleshell.builder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.raysha.lib.jsimpleshell.Shell;
import jline.console.ConsoleReader;

/**
 * This is a part of the {@link ShellBuilder}. It is responsible for configuring
 * all stuff that has to do with the input/output of the shell.
 *
 * @author rainu
 */
public class IO implements Builder {
	private final BuilderModel model;
	private final Builder parentBuilder;

	IO(BuilderModel model, Builder parentBuilder) {
		this.model = model;
		this.parentBuilder = parentBuilder;
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

	@Override
	public Behavior behavior() {
		return new Behavior(model, this);
	}

	@Override
	public IO io() {
		return this;
	}

	@Override
	public Look look() {
		return new Look(model, this);
	}

	@Override
	public ShellBuilder root() {
		if(parentBuilder instanceof ShellBuilder)
			return (ShellBuilder) parentBuilder;

		return parentBuilder.root();
	}

	@Override
	public Shell build() {
		return parentBuilder.build();
	}
}
