package de.raysha.lib.jsimpleshell.example;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.CLIException;

import java.io.File;
import java.io.IOException;

public class Starter {
	public static void main(String[] args) throws IOException, CLIException {
		final Shell shell = ShellBuilder.shell("JSS")
								.behavior()
									.addHandler(new Commands())
								.build();

		//print help text
		shell.processLine("?help");

		if(args.length > 0){
			String[] scriptArgs = null;
			if(args.length > 1){
				scriptArgs = new String[args.length - 1];
				for(int i=1; i < args.length; i++){
					scriptArgs[i-1] = args[i];
				}
			}else{
				scriptArgs = new String[]{};
			}

			shell.runScript(new File(args[0]), scriptArgs);
		}

		//processLine will be work fine if no subshell is starting
		shell.processLine(".echo Hello");

		//... but if you process a command which starts a subshell,
		//you have to use the command pipeline!
		shell.getPipeline().append("build");	//starts a subshell
			shell.getPipeline().append("sfn Max");
			shell.getPipeline().append("sln Test");
		shell.getPipeline().append("commit");	//leave the subshell

		//now its the users turn...
		shell.commandLoop();
	}
}
