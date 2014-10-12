package de.raysha.lib.jsimpleshell.completer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;

/**
 * This {@link CandidatesChooser} is responsible for auto complete boolean values (true/false).
 *
 * @author rainu
 */
public class BooleanCandidatesChooser implements CandidatesChooser {

	/**
	 * Type for a {@link Boolean} parameter.
	 */
	public static final String BOOLEAN_TYPE = "java.lang.Boolean";

	@Override
	public Candidates chooseCandidates(ShellCommandParamSpec paramSpec, String part) {
		if(!responsibleFor(paramSpec)) return null;

		List<String> values = new ArrayList<String>(2);
		values.add("true");
		values.add("false");

		Iterator<String> iter = values.iterator();
		while(iter.hasNext()){
			if(part != null && !iter.next().startsWith(part.toLowerCase())){
				iter.remove();
			}
		}

		return new Candidates(values);
	}

	private boolean responsibleFor(ShellCommandParamSpec paramSpec) {
		return Boolean.class == paramSpec.getValueClass() ||
				boolean.class == paramSpec.getValueClass() ||
				BOOLEAN_TYPE.equals(paramSpec.getType());
	}

}