package de.raysha.lib.jsimpleshell.completer;

import java.util.ArrayList;
import java.util.List;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;

/**
 * This {@link CandidatesChooser} delegates all calls to a list of {@link CandidatesChooser}.
 *
 * @author rainu
 *
 */
public class AggregateCandidatesChooser implements CandidatesChooser {
	private List<CandidatesChooser> delegates;

	public AggregateCandidatesChooser(){
		this.delegates = new ArrayList<CandidatesChooser>();
	}

	public AggregateCandidatesChooser(List<CandidatesChooser> delegates){
		this.delegates = delegates;
	}

	public void addCandidatesChooser(CandidatesChooser chooser){
		this.delegates.add(chooser);
	}

	@Override
	public Candidates chooseCandidates(ShellCommandParamSpec spec, String part) {
		for(CandidatesChooser chooser : delegates){
			Candidates candidates = chooser.chooseCandidates(spec, part);
			if(candidates != null && !candidates.getValues().isEmpty()){
				return candidates;
			}
		}

		return new Candidates(new ArrayList<String>());
	}

}
