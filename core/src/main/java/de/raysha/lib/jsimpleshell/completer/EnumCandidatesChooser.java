package de.raysha.lib.jsimpleshell.completer;

import java.util.ArrayList;
import java.util.List;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;

/**
 * This {@link CandidatesChooser} is responsible for auto complete {@link Enum} elements.
 *
 * @author rainu
 */
public class EnumCandidatesChooser implements CandidatesChooser {

	@Override
	public Candidates chooseCandidates(ShellCommandParamSpec paramSpec, String part) {
		if(!responsibleFor(paramSpec)) return null;

		List<String> elements = new ArrayList<String>();

		for(Object e : paramSpec.getValueClass().getEnumConstants()){
			String representation = ((Enum<?>)e).name();

			if(representation.toUpperCase().startsWith(part.toUpperCase())){
				elements.add(representation);
			}
		}

		return new Candidates(elements);
	}

	private boolean responsibleFor(ShellCommandParamSpec paramSpec) {
		return paramSpec.getValueClass().isEnum();
	}

}
