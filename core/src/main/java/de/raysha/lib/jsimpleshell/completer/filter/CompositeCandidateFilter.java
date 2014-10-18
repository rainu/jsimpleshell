package de.raysha.lib.jsimpleshell.completer.filter;

import java.util.LinkedList;
import java.util.List;

/**
 * This {@link CandidateFilter} delegates all requests to his list of {@link CandidateFilter}.
 *
 * @author rainu
 */
public class CompositeCandidateFilter implements CandidateFilter {

	private final List<CandidateFilter> filterChain = new LinkedList<CandidateFilter>();

	public List<CandidateFilter> getFilterChain() {
		return filterChain;
	}

	@Override
	public boolean filter(FilterContext context) {
		for(CandidateFilter filter : filterChain){
			if(!filter.filter(context)){
				return false;
			}
		}

		return true;
	}
}
