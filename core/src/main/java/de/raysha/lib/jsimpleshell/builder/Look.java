package de.raysha.lib.jsimpleshell.builder;

import de.raysha.lib.jsimpleshell.PromptElement;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.util.PromptBuilder;

/**
 * This is a part of the {@link ShellBuilder}. It is responsible for configuring
 * all stuff that has to do with the look of the shell.
 *
 * @author rainu
 */
public class Look implements Builder {
	private final BuilderModel model;
	private final Builder parentBuilder;

	Look(BuilderModel model, Builder parentBuilder) {
		this.model = model;
		this.parentBuilder = parentBuilder;
	}

	/**
	 * Set the app name. This name will be shown if the shell will be entered.
	 *
	 * @param appName The app name.
	 * @return This {@link Look}
	 */
	public Look setAppName(String appName) {
		model.setAppName(appName);
		return this;
	}

	/**
	 * Change the used {@link PromptElement}.
	 *
	 * @param prompt The {@link PromptElement} that should be used.
	 * @return This {@link Look}
	 */
	public Look setPrompt(PromptElement prompt) {
		model.setPrompt(prompt);

		return this;
	}

	/**
	 * Change the used prompt.
	 *
	 * @param prompt The prompt that should be used.
	 * @return This {@link Look}
	 */
	public Look setPrompt(String prompt) {
		model.setPrompt(PromptBuilder.fromString(prompt));

		return this;
	}

	/**
	 * Disable color output. No colored text will be printed even if the
	 * color-output-methods was called.
	 *
	 * @return This {@link Look}
	 */
	public Look disableColor() {
		model.setColorOutput(false);

		return this;
	}

	/**
	 * Enable color output.
	 *
	 * @return This {@link Look}
	 */
	public Look enableColor() {
		model.setColorOutput(true);

		return this;
	}

	/**
	 * Enable or disable color output.
	 *
	 * @param enable True if the color should be enabled. Otherwise false.
	 * @return This {@link Look}
	 */
	public Look setColor(boolean enable){
		if(enable){
			enableColor();
		}else{
			disableColor();
		}

		return this;
	}

	@Override
	public Behavior behavior() {
		return new Behavior(model, this);
	}

	@Override
	public IO io() {
		return new IO(model, this);
	}

	@Override
	public Look look() {
		return this;
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
