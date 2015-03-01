package de.raysha.lib.jsimpleshell.completer;

import java.util.ArrayList;
import java.util.List;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;

/**
 * This {@link CandidatesChooser} is responsible for auto complete {@link Enum} elements.
 *
 * @author rainu
 */
public class EnumCandidatesChooser extends AbstractCandidatesChooser {

	@Override
	public Candidates chooseCandidates(ShellCommandParamSpec paramSpec, String part) {
		if(!responsibleFor(paramSpec)) return null;

		List<String> elements = new ArrayList<String>();

		Class<?> pClass = getParameterClass(paramSpec);
		for(Object e : pClass.getEnumConstants()){
			String representation = ((Enum<?>)e).name();

			if(representation.toUpperCase().startsWith(part.toUpperCase())){
				elements.add(representation);
			}
		}

		return new Candidates(elements);
	}

	protected boolean responsibleFor(ShellCommandParamSpec paramSpec) {
		Class<?> pClass = paramSpec.getValueClass();

		if(pClass.isArray()){
			return pClass.getComponentType().isEnum();
		}
		return pClass.isEnum();
	}

}
