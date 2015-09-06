package de.raysha.lib.jsimpleshell.example;

import de.raysha.lib.jsimpleshell.annotation.Command;

public class Commands {

	@Command
	public String noAccess(){
		return "You should not be able to execute this command!";
	}

	@Command
	public String restrictedAccess(){
		return "Access granted!";
	}
}
