package jsimpleshell.example;

import java.io.IOException;

import jsimpleshell.ShellFactory;
import asg.cliche.example.HelloWorld;

public class Starter {

	public static void main(String[] args) throws IOException {
		System.out.println("   _____   ______   __                          __             ______   __                  __  __  ");
		System.out.println("   |     \\ /      \\ |  \\                        |  \\           /      \\ |  \\                |  \\|  \\");
		System.out.println("    \\$$$$$|  $$$$$$\\ \\$$ ______ ____    ______  | $$  ______  |  $$$$$$\\| $$____    ______  | $$| $$");
		System.out.println("      | $$| $$___\\$$|  \\|      \\    \\  /      \\ | $$ /      \\ | $$___\\$$| $$    \\  /      \\ | $$| $$");
		System.out.println(" __   | $$ \\$$    \\ | $$| $$$$$$\\$$$$\\|  $$$$$$\\| $$|  $$$$$$\\ \\$$    \\ | $$$$$$$\\|  $$$$$$\\| $$| $$");
		System.out.println("|  \\  | $$ _\\$$$$$$\\| $$| $$ | $$ | $$| $$  | $$| $$| $$    $$ _\\$$$$$$\\| $$  | $$| $$    $$| $$| $$");
		System.out.println("| $$__| $$|  \\__| $$| $$| $$ | $$ | $$| $$__/ $$| $$| $$$$$$$$|  \\__| $$| $$  | $$| $$$$$$$$| $$| $$");
		System.out.println(" \\$$    $$ \\$$    $$| $$| $$ | $$ | $$| $$    $$| $$ \\$$     \\ \\$$    $$| $$  | $$ \\$$     \\| $$| $$");
		System.out.println("  \\$$$$$$   \\$$$$$$  \\$$ \\$$  \\$$  \\$$| $$$$$$$  \\$$  \\$$$$$$$  \\$$$$$$  \\$$   \\$$  \\$$$$$$$ \\$$ \\$$");
		System.out.println("                                      | $$                                                          ");
		System.out.println("                                      | $$                                                          ");
		System.out.println("                                       \\$$                                                          ");		
		
		ShellFactory.createShell(">", "superShell", new HelloWorld()).commandLoop();
	}

}
