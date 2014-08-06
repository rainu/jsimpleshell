package de.raysha.lib.jsimpleshell;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.handler.OutputDependent;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;

public class OutputCommands implements OutputDependent {

	private OutputBuilder output;
	
	@Override
	public void cliSetOutput(OutputBuilder output) {
		this.output = output;
	}
	

	@Command
	public void outColor(){
		output.enableColor();
		output.out().black("text").println();
    	output.out().blue("text").println();
    	output.out().cyan("text").println();
    	output.out().green("text").println();
    	output.out().magenta("text").println();
    	output.out().red("text").println();
    	output.out().white("text").println();
    	output.out().yellow("text").println();
    	
    	output.out().blackBG("text").println();
    	output.out().blueBG("text").println();
    	output.out().cyanBG("text").println();
    	output.out().greenBG("text").println();
    	output.out().magentaBG("text").println();
    	output.out().redBG("text").println();
    	output.out().whiteBG("text").println();
    	output.out().yellowBG("text").println();
	}
	
	@Command
	public void outWithoutColor(){
		output.disableColor();
		output.out().black("text").println();
    	output.out().blue("text").println();
    	output.out().cyan("text").println();
    	output.out().green("text").println();
    	output.out().magenta("text").println();
    	output.out().red("text").println();
    	output.out().white("text").println();
    	output.out().yellow("text").println();
    	
    	output.out().blackBG("text").println();
    	output.out().blueBG("text").println();
    	output.out().cyanBG("text").println();
    	output.out().greenBG("text").println();
    	output.out().magentaBG("text").println();
    	output.out().redBG("text").println();
    	output.out().whiteBG("text").println();
    	output.out().yellowBG("text").println();
	}
	
	@Command
	public void errColor(){
		output.enableColor();
		output.err().black("text").println();
    	output.err().blue("text").println();
    	output.err().cyan("text").println();
    	output.err().green("text").println();
    	output.err().magenta("text").println();
    	output.err().red("text").println();
    	output.err().white("text").println();
    	output.err().yellow("text").println();
    	
    	output.err().blackBG("text").println();
    	output.err().blueBG("text").println();
    	output.err().cyanBG("text").println();
    	output.err().greenBG("text").println();
    	output.err().magentaBG("text").println();
    	output.err().redBG("text").println();
    	output.err().whiteBG("text").println();
    	output.err().yellowBG("text").println();
	}
	
	@Command
	public void errWithoutColor(){
		output.disableColor();
		output.err().black("text").println();
    	output.err().blue("text").println();
    	output.err().cyan("text").println();
    	output.err().green("text").println();
    	output.err().magenta("text").println();
    	output.err().red("text").println();
    	output.err().white("text").println();
    	output.err().yellow("text").println();
    	
    	output.err().blackBG("text").println();
    	output.err().blueBG("text").println();
    	output.err().cyanBG("text").println();
    	output.err().greenBG("text").println();
    	output.err().magentaBG("text").println();
    	output.err().redBG("text").println();
    	output.err().whiteBG("text").println();
    	output.err().yellowBG("text").println();
	}
}
