package de.raysha.lib.jsimpleshell.example;

import de.raysha.lib.jsimpleshell.handler.CommandAccessManager;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision.Decision;

/**
 * This class is responsible for grand access to command.
 *
 * @author rainu
 */
public class MyAccessManager implements CommandAccessManager {

	private boolean toggle = true;

	@Override
	public AccessDecision checkCommandPermission(Context context) {
		if(context.getCommandParameters() == null){
			/*
			 * This check must quick because it is the check against each available command!
			 */
			return checkGeneralAccess(context);
		}

		if(context.getCommand().getMethod().getName().equals("noAccess")){
			return new AccessDecision(Decision.DENIED, "No one have access to this command!");
		}

		if(context.getCommand().getMethod().getName().equals("restrictedAccess")){
			//only every second call will be allowed
			toggle = !toggle;
			return new AccessDecision(toggle ? Decision.ALLOWED : Decision.DENIED);
		}

		//all other commands will be allowed
		return new AccessDecision(Decision.ALLOWED);
	}

	private AccessDecision checkGeneralAccess(Context context) {
		if(context.getCommand().getMethod().getName().equals("noAccess")){
			return new AccessDecision(Decision.DENIED, "No one have access to this command!");
		}

		return new AccessDecision(Decision.ALLOWED);
	}

}
