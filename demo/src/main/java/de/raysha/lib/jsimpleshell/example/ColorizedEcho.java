package de.raysha.lib.jsimpleshell.example;

import de.raysha.lib.jsimpleshell.annotation.Command;

/**
 * This is a another container for commands.
 * A SubShell can perfectly be used for the builder-pattern :)
 *
 * @author rainu
 *
 */
public class ColorizedEcho {

	private StringBuilder sb = new StringBuilder();

	@Command
	public void writeInBlue(String value){
		sb.append("\u001B[34m" + value + "\u001B[0m");
	}

	@Command
	public void writeInGreen(String value){
		sb.append("\u001B[32m" + value + "\u001B[0m");
	}

	@Command
	public void writeNormal(String value){
		sb.append(value);
	}

	//Only Methods with @Command are commands that can be executed by a user
	public String build(){
		return sb.toString();
	}
}
