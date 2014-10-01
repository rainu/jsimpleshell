package de.raysha.lib.jsimpleshell;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision.Decision;

public class SecurityCommands implements CommandAccessManager {

	private boolean loggedIn = false;

	@Override
	public AccessDecision checkCommandPermission(Context context) {
		if(context.getCommand().getName().equals("do-something")){
			if(!loggedIn){
				return new AccessDecision(Decision.DENIED, "You are not logged in!");
			}
		}

		return new AccessDecision(Decision.ALLOWED);
	}

	@Command
	public String login(){
		loggedIn = true;

		return "Welcome!";
	}

	@Command
	public String doSomething(){
		return "Read private messages...";
	}
}
