package de.rainu.lib.jsimpleshell.example;

import java.io.File;
import java.io.IOException;

import de.rainu.lib.jsimpleshell.Info;
import de.rainu.lib.jsimpleshell.Shell;
import de.rainu.lib.jsimpleshell.ShellBuilder;
import de.rainu.lib.jsimpleshell.exception.CLIException;

public class Starter {
	private static final String VERSION = Info.getVersion();
	private static final String PROMT = "$> ";
	
	public static void main(String[] args) throws IOException, CLIException {
		System.out.println("    _____   ______   __                          __             ______   __                  __  __  ");
		System.out.println("   |     \\ /      \\ |  \\     DEMO               |  \\           /      \\ |  \\                |  \\|  \\");
		System.out.println("    \\$$$$$|  $$$$$$\\ \\$$ ______ ____    ______  | $$  ______  |  $$$$$$\\| $$____    ______  | $$| $$");
		System.out.println("      | $$| $$___\\$$|  \\|      \\    \\  /      \\ | $$ /      \\ | $$___\\$$| $$    \\  /      \\ | $$| $$");
		System.out.println(" __   | $$ \\$$    \\ | $$| $$$$$$\\$$$$\\|  $$$$$$\\| $$|  $$$$$$\\ \\$$    \\ | $$$$$$$\\|  $$$$$$\\| $$| $$");
		System.out.println("|  \\  | $$ _\\$$$$$$\\| $$| $$ | $$ | $$| $$  | $$| $$| $$    $$ _\\$$$$$$\\| $$  | $$| $$    $$| $$| $$");
		System.out.println("| $$__| $$|  \\__| $$| $$| $$ | $$ | $$| $$__/ $$| $$| $$$$$$$$|  \\__| $$| $$  | $$| $$$$$$$$| $$| $$");
		System.out.println(" \\$$    $$ \\$$    $$| $$| $$ | $$ | $$| $$    $$| $$ \\$$     \\ \\$$    $$| $$  | $$ \\$$     \\| $$| $$");
		System.out.println("  \\$$$$$$   \\$$$$$$  \\$$ \\$$  \\$$  \\$$| $$$$$$$  \\$$  \\$$$$$$$  \\$$$$$$  \\$$   \\$$  \\$$$$$$$ \\$$ \\$$");
		System.out.println("                                      | $$");
		System.out.println("https://github.com/rainu/jsimpleshell | $$    Version: " + VERSION);
		System.out.println("                                       \\$$");		
		System.out.println();
		System.out.println("Usage:");
		System.out.println("* You can use \"?list\" to show available commands. The command \"?list-all\" lists also the special commands.");
		System.out.println("* If you want to know more about a specific command, you can use the \"?help\" command. For example:");
		System.out.println("\t" + PROMT + "?help echo");
		System.out.println("* Don't be shy to use the arrow-keys! Seriously, try it out :)");
		System.out.println("\t<LEFT_ARROW> and <RIGHT_ARROW> navigates in current line");
		System.out.println("\t<UP_ARROW> and <DOWN_ARROW> navigates through the command history");
		System.out.println("* Use <TAB> to auto-complete the available commands or file/directory path");
		
		System.out.println("* To exit this shell, use \"exit\" :)");
		System.out.println();
		
		final Shell shell = ShellBuilder.shell("JSS")
								.addHandler(new MainShell())
								.addHandler(new Calendar())	//this handler is also a InputTypeConverter
							.build();
		
		if(args.length > 0){
			shell.runScript(new File(args[0]));
		}
	
		shell.commandLoop();
	}
}
