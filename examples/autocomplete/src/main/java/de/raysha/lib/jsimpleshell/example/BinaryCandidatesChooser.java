package de.raysha.lib.jsimpleshell.example;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;
import de.raysha.lib.jsimpleshell.completer.AbstractCandidatesChooser;

/**
 * This class is responsible for auto complete our special binary type.
 *
 * @author rainu
 */
public class BinaryCandidatesChooser extends AbstractCandidatesChooser {

	public static final String BINARY_TYPE = "de.raysha.lib.jsimpleshell.example.BinaryCandidatesChooser";

	private Set<String> values;

	public BinaryCandidatesChooser() {
		super(BINARY_TYPE);

		//prefill our static auto complete values
		Set<String> tmpValues = new HashSet<String>();
		for(int i=0; i < 256; i++){
			tmpValues.add(String.format("%8s", Integer.toBinaryString(i)).replace(" ", "0"));
		}
		values = Collections.unmodifiableSet(tmpValues);
	}


	@Override
	public Candidates chooseCandidates(ShellCommandParamSpec paramSpec, String part) {
		/*
		 * As an CandidatesChooser i will get ALL (user) input strings.
		 * But i am not responsible for all targets!
		 */
		if(!responsibleFor(paramSpec)){
			//if i am not responsible for, i must return null!
			return null;
		}

		Set<String> possibleCandidates = new HashSet<String>(values);
		Iterator<String> iter = possibleCandidates.iterator();
		while(iter.hasNext()){
			if(!iter.next().startsWith(part)){
				iter.remove();
			}
		}

		return new Candidates(possibleCandidates);
	}

}
