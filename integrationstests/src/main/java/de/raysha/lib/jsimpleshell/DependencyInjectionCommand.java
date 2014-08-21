package de.raysha.lib.jsimpleshell;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.io.InputBuilder;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;

public class DependencyInjectionCommand {

	@Inject
	private Shell shell;

	private InputBuilder input;

	@Inject
	private OutputBuilder output;

	@javax.inject.Inject
	private MessageResolver messageResolver;

	@Command
	public String checkDependencies(){
		if(shell == null){
			return "shell is null!";
		}

		if(input == null){
			return "input is null!";
		}

		if(output == null){
			return "output is null!";
		}

		if(messageResolver == null){
			return "messageResolver is null!";
		}

		return null;
	}

	@Inject
	public void setInput(InputBuilder input) {
		this.input = input;
	}

	@Inject
	public void setIO(InputBuilder input, OutputBuilder output) {
		this.input = input;
		this.output = output;
	}
}
