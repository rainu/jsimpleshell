package de.raysha.lib.jsimpleshell.handler.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager;
import de.raysha.lib.jsimpleshell.handler.CommandValidator;

/**
 * This {@link CommandValidator} delegates all request to a list of
 * {@link CommandValidator}s.
 *
 * @author rainu
 */
public class CompositeCommandValidator implements CommandValidator {
	private final List<CommandValidator> delegates;

	public CompositeCommandValidator(){
		this.delegates = new LinkedList<CommandValidator>();
	}

	/**
	 * Add a {@link CommandValidator} as a delegate.
	 *
	 * @param validator The command validator.
	 */
	public void addCommandValidator(CommandValidator validator){
		this.delegates.add(validator);
	}

	/**
	 * Remove a {@link CommandAccessManager} from the delegates.
	 *
	 * @param validator The command validator.
	 */
	public void removeCommandValidator(CommandValidator validator){
		this.delegates.remove(validator);
	}

	@Override
	public ValidationResult validate(Context context) {
		ValidationResult finalResult = ValidationResult.forContext(context);

		for(CommandValidator validator : delegates){
			ValidationResult result = validator.validate(context);

			for(Entry<ShellCommandParamSpec, String> failure : result.getFailures().entrySet()){
				finalResult.addFailure(failure.getKey(), failure.getValue());
			}
		}

		return finalResult;
	}

}
