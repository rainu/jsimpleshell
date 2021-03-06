package de.raysha.lib.jsimpleshell;

import java.io.IOException;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.io.InputBuilder;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;

public class InputCommands {

	@Inject private InputBuilder input;
	@Inject private OutputBuilder output;

	@Command
	public void masked() throws IOException{
		output.out().normal("Enter: ").print();
		String read = input.maskedIn('*').readLine();
		output.out().normal("Password: ").normal(read).println();
	}

	@Command
	public void invisible() throws IOException{
		output.out().normal("Enter: ").print();
		String read = input.invisibleIn().readLine();
		output.out().normal("Password: ").normal(read).println();
	}

	@Command
	public void visible() throws IOException{
		String read = input.in().withPromt("Enter: ").readLine();
		output.out().normal("Password: ").normal(read).println();
	}

	@Command
	public void maskedWithHistory() throws IOException{
		output.out().normal("Enter: ").print();
		String read = input.maskedIn('*').saveHistory().readLine();
		output.out().normal("Password: ").normal(read).println();
	}

	@Command
	public void invisibleWithHistory() throws IOException{
		output.out().normal("Enter: ").print();
		String read = input.invisibleIn().saveHistory().readLine();
		output.out().normal("Password: ").normal(read).println();
	}

	@Command
	public void visibleWithHistory() throws IOException{
		String read = input.in().saveHistory().withPromt("Enter: ").readLine();
		output.out().normal("Password: ").normal(read).println();
	}
}
