package de.rainu.lib.jsimpleshell.example;

import java.io.File;
import java.io.IOException;

import de.rainu.lib.jsimpleshell.Shell;
import de.rainu.lib.jsimpleshell.ShellBuilder;
import de.rainu.lib.jsimpleshell.exception.CLIException;

public class Starter {
	public static void main(String[] args) throws IOException, CLIException {
		final Shell shell = ShellBuilder.shell("JSS")
								.addHandler(new MainShell())
								.addHandler(new Calendar())	//this handler is also a InputTypeConverter
							.build();
		
		//print help text
		shell.processLine("?help");
		
		if(args.length > 0){
			shell.runScript(new File(args[0]));
		}
	
		shell.commandLoop();
	}
}
