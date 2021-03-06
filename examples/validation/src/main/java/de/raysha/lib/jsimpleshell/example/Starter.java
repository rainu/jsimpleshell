package de.raysha.lib.jsimpleshell.example;

import java.io.File;
import java.io.IOException;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.CLIException;

public class Starter {
	public static void main(String[] args) throws IOException, CLIException {
		final Shell shell = ShellBuilder.shell("JSS")
								.behavior()
									.addAuxHandler(new CustomValidator()) //register our own validator
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

		shell.commandLoop();
	}
}
