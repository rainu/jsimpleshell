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
public class VariableCandidatesChooser extends AbstractCandidatesChooser {

	public static final String VARIABLE_NAME_TYPE = "de.raysha.lib.jsimpleshell.completer.VariableCandidatesChooser_variableName";

	@Inject private Environment environment;

	public VariableCandidatesChooser() {
		super(VARIABLE_NAME_TYPE);
	}

	@Override
	public Candidates chooseCandidates(ShellCommandParamSpec paramSpec, String part) {
		if(!responsibleFor(paramSpec, part)) return null;

		List<String> values = null;

		if(part.startsWith("$")){
			values = getVariables(paramSpec, part);
		}else{
			values = getVariableNames(part);
		}

		Candidates candidates = new Candidates(values);
		return candidates;
	}

	private List<String> getVariables(ShellCommandParamSpec paramSpec, String part) {
		final String varNamePart = part.substring(1);

		List<Variable> possibleVars = new ArrayList<Variable>();

		for(Variable var : environment.getVariables()){
			if(var.getName().startsWith(varNamePart)){
				boolean add = false;

				if(var.getValue() == null){
					add = true;
				}else {
					final Class<?> paramClass = getParameterClass(paramSpec);
					final Class<?> varClass = var.getValue().getClass();

					if(paramClass.isAssignableFrom(varClass)){
						add = true;
					}
				}

				if(add){
					possibleVars.add(var);
				}
			}
		}

		List<String> names = new ArrayList<String>(possibleVars.size());
		for(Variable var : possibleVars){
			names.add("$" + var.getName());
		}

		return names;
	}

	private List<String> getVariableNames(String part) {
		List<String> names = new ArrayList<String>();
		for(Variable var : environment.getVariables()){
			if(var.getName().startsWith(part)){
				names.add(var.getName());
			}
		}

		return names;
	}

	private boolean responsibleFor(ShellCommandParamSpec paramSpec, String part) {
		return responsibleFor(paramSpec) || part.startsWith("$");
	}
}
