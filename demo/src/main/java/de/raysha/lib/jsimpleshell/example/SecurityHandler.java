package de.raysha.lib.jsimpleshell.example;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision.Decision;

/**
 * This handler demonstrate the functionality of a {@link CommandAccessManager}.
 * Such handler can decide if a command can be execute or not.
 *
 * @author rainu
 */
public class SecurityHandler implements CommandAccessManager {

	private boolean loggedIn = false;

	@Override
	public AccessDecision checkCommandPermission(Context context) {
		//the context object contains all information about the current command

		if("do-secure-stuff".equals(context.getCommand().getName())){
			if(!loggedIn){
				//if you want to deny these command, you must return a Denied-AccessDecision
				return new AccessDecision(Decision.DENIED, "You must log in before!");
			}
		}

		//if you want to allow these command you must return a Allowed-AccessDecision
		return new AccessDecision(Decision.ALLOWED);
	}

	@Command
	public String login(){
		loggedIn = true;

		return "Now you have access to the >do-secure-stuff< command!";
	}

	@Command
	public String doSecureStuff(){
		//the "checkCommandPermission" method is responsible for protecting me!

		return "I am only visible after login.";
	}
}
