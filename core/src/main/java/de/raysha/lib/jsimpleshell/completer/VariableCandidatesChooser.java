package de.raysha.lib.jsimpleshell.completer;

import java.util.ArrayList;
import java.util.List;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.script.Environment;
import de.raysha.lib.jsimpleshell.script.Variable;

/**
 * This {@link CandidatesChooser} is responsible for complete variable names from the current environment.
 *
 * @author rainu
 */
public class VariableCandidatesChooser implements CandidatesChooser {

	public static final String VARIABLE_NAME_TYPE = "de.raysha.lib.jsimpleshell.completer.VariableCandidatesChooser_variableName";

	@Inject private Environment environment;

	@Override
	public Candidates chooseCandidates(ShellCommandParamSpec paramSpec, String part) {
		if(!responsibleFor(paramSpec)) return null;

		List<String> names = new ArrayList<String>();
		for(Variable var : environment.getVariables()){
			if(var.getName().startsWith(part)){
				names.add(var.getName());
			}
		}

		Candidates candidates = new Candidates(names);
		return candidates;
	}

	private boolean responsibleFor(ShellCommandParamSpec paramSpec) {
		return VARIABLE_NAME_TYPE.equals(paramSpec.getType());
	}
}
