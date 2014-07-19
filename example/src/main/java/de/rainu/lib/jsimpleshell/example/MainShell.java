package de.rainu.lib.jsimpleshell.example;

import java.io.IOException;

import de.rainu.lib.jsimpleshell.Shell;
import de.rainu.lib.jsimpleshell.ShellBuilder;
import de.rainu.lib.jsimpleshell.ShellDependent;
import de.rainu.lib.jsimpleshell.annotation.Command;
import de.rainu.lib.jsimpleshell.annotation.Param;
import de.rainu.lib.jsimpleshell.io.OutputBuilder;
import de.rainu.lib.jsimpleshell.io.OutputDependent;

/**
 * This class contains all Commands for the main shell.
 * 
 * @author rainu
 */
//if you want that the shell make sub shell's you must implement the ShellDependent interface 
//to get the parent shell that is needed if you want to create a subshell

//if you want to make output within a command you must implement the OutputDependent interface
//to get the OutputBuilder. With that builder you can print output to out/err
public class MainShell implements ShellDependent, OutputDependent {
	private Shell shell;
	private OutputBuilder output;
	
	@Override
	public void cliSetShell(Shell theShell) {
		this.shell = theShell;
	}
	
	@Override
	public void cliSetOutput(OutputBuilder output) {
		this.output = output;		
	}
	
    @Command(abbrev = "e", description = "Print the argument to std-out.")
    //if you have not define a seperate name in @Command the method-name itself will be used as command name
    public String echo(
    		@Param(name = "value", description = "The string which should be printed.")
    		String...strings) {
    	
    	//the return value of a command-method will be printed out on std-out
        StringBuilder sb = new StringBuilder();
        if(strings != null) for(String s : strings){
        	sb.append(s);
        	sb.append(" ");
        }
    	
    	return sb.toString();
    }
    
    @Command(abbrev = "ce", 
    		name = "cecho", //the user must enter "cecho" to start this command
    		description = "Start a new subshell. Here you can build a colored string that will printed out when you exit that shell.", 
    		//the header will always shown if the user run this command
    		header = "Each subshell have his own commands. Use \"?list\" to show you which commands are available! Use \"exit\" to get out of this shell.")
    public void colorizedEcho() throws IOException {
    	final ColorizedEcho echoBuilder = new ColorizedEcho();
    	
    	Shell subShell = ShellBuilder.subshell("cecho", shell)
							.addHandler(echoBuilder)
						.build();

    	//the method will be blocked until the shell was abandoned
    	subShell.commandLoop();
    	
    	//this command doesnt return the output but use the output directly (this is possible because this class implements the OutputDependent-Interface)
    	output.out().normal(echoBuilder.build()).println();
    }
    
    @Command(abbrev = "ct", description = "Print colored text.")
    public void printColorText(){
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
}
