package de.raysha.lib.jsimpleshell.script;

import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.exception.CLIException;
import de.raysha.lib.jsimpleshell.handler.InputConverter;
import de.raysha.lib.jsimpleshell.io.OutputConversionEngine;

/**
 * This {@link InputConverter} is responsible for resolving variables as parameter.
 *
 * @author rainu
 */
public class VariableInputConverter implements InputConverter {

	@Inject
	private Environment environment;

	@Inject
	private OutputConversionEngine outputEngine;

	@Override
	public Object convertInput(String original, Class<?> toClass) throws Exception {
		if(!isVariable(original)) return null;

		final String varName = extractVariableName(original);
		Variable var = environment.getVariable(varName);

		if(var == null || var.getValue() == null) {
			return null;
		}

		if(!toClass.isAssignableFrom(var.getValue().getClass())){
			if(toClass == String.class){
				Object output = outputEngine.convertOutput(var.getValue());
				if(output.getClass() == String.class){
					return output;
				}else{
					return var.getValue().toString();
				}
			}

			throw new CLIException("The variable type doesn't match the expected class " + toClass.getName());
		}

		return var.getValue();
	}

	private boolean isVariable(String input) {
		return input != null && input.startsWith("$");
	}

	private String extractVariableName(String input) {
		return input.substring(1); //cut the variable-sign "$"
	}

}
