package de.rainu.lib.jsimpleshell.example;

import java.io.IOException;

import de.rainu.lib.jsimpleshell.Shell;
import de.rainu.lib.jsimpleshell.ShellDependent;
import de.rainu.lib.jsimpleshell.ShellFactory;
import de.rainu.lib.jsimpleshell.annotation.Command;
import de.rainu.lib.jsimpleshell.annotation.Param;

/**
 * This class contains all Commands for the main shell.
 * 
 * @author rainu
 */
//if you want that the shell make sub shell's you must implement the ShellDependent interface 
//to get the parent shell that is needed if you want to create a subshell
public class MainShell implements ShellDependent {
	private Shell parent;
	
	@Override
	public void cliSetShell(Shell theShell) {
		this.parent = theShell;
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
    public String colorizedEcho() throws IOException {
    	final ColorizedEcho echoBuilder = new ColorizedEcho();
    	
    	Shell subShell = ShellFactory.createSubshell("subshell", parent, null, echoBuilder);

    	//the method will be blocked until the shell was abandoned
    	subShell.commandLoop();
    	
    	return echoBuilder.build();
    }
}
