package de.raysha.lib.jsimpleshell.example;

import de.raysha.lib.jsimpleshell.handler.CommandValidator;

/**
 * This is our custom {@link CommandValidator}. This will be called
 * for each command (even if the command has jsr-303 annotations).
 *
 * @author rainu
 */
public class CustomValidator implements CommandValidator {

	@Override
	public ValidationResult validate(Context ctx) {
		final ValidationResult result = ValidationResult.forContext(ctx);

		if(!responsibleFor(ctx)){
			//if i am not responsible for, i must return an "empty" validation result! (never null)
			return result;
		}

		//get the user input (if the user type any string, this code will never reached)
		final Integer input = (Integer)ctx.getCommandParameters()[0];

		//validation logic...
		if(input < 1 || input > 100){

			//validation fails! -> we have to register a failure (including a message)
			//the message can also be an key for a message-resolver (see example-project for message resolver)
			result.addFailure(ctx.getCommand().getParamSpecs()[0], "must be => 1 and <= 100");
		}

		return result;
	}

	/*
	 * As an CommandValidator i will get ALL validation-requests.
	 * But i am not responsible for all of them!
	 */
	private boolean responsibleFor(Context ctx) {
		return "myPercent".equals(ctx.getCommand().getMethod().getName());
	}

}
