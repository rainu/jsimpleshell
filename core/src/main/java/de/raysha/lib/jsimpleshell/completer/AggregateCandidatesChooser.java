package de.raysha.lib.jsimpleshell.completer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
	public Collection<String> chooseCandidates(ShellCommandParamSpec spec, String part) {
		Collection<String> candidates = new HashSet<String>();
		
		for(CandidatesChooser chooser : delegates){
			candidates.addAll(chooser.chooseCandidates(spec, part));
		}
		
		return candidates;
	}

}
