package de.raysha.lib.jsimpleshell;

import java.io.IOException;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.handler.InputDependent;
import de.raysha.lib.jsimpleshell.handler.OutputDependent;
import de.raysha.lib.jsimpleshell.io.InputBuilder;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;

public class InputCommands implements InputDependent, OutputDependent {

	private InputBuilder input;
	private OutputBuilder output;
	
	@Override
	public void cliSetInput(InputBuilder input) {
		this.input = input;
	}
	
	@Override
	public void cliSetOutput(OutputBuilder output) {
		this.output = output;
	}
	

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
}
