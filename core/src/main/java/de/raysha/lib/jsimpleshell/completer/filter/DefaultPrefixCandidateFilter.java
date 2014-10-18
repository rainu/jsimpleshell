package de.raysha.lib.jsimpleshell.completer.filter;

import java.util.Arrays;
import java.util.Collection;

import de.raysha.lib.jsimpleshell.util.CommandChainInterpreter;

/**
 * This {@link CandidateFilter} filters all candidates out which have an
 * prefix (e.g. "?") and the user don't typed this prefix before.
 *
 * @author rainu
 */
public class DefaultPrefixCandidateFilter implements CandidateFilter {
	private final Collection<String> prefixes;

	public DefaultPrefixCandidateFilter(String...prefixes) {
		this.prefixes = Arrays.asList(prefixes);
	}

	@Override
	public boolean filter(FilterContext context) {
		if(startCandidateWithPrefix(context)){
			if(!startPartWithPrefix(context)){
				return false;
			}
		}

		return true;
	}

	private boolean startPartWithPrefix(FilterContext context) {
		String realPart = getRealPart(context);

		return startsWithPrefix(realPart.trim());
	}

	private String getRealPart(FilterContext context) {
		String line = context.getPart().substring(0, context.getCursor());

		if(line.contains(" " + CommandChainInterpreter.OPERATION_AND)){
			line = line.substring(line.lastIndexOf(CommandChainInterpreter.OPERATION_AND));

			line = line.substring(CommandChainInterpreter.OPERATION_AND.length());
		}else if(line.contains(" " + CommandChainInterpreter.OPERATION_OR)){
			line = line.substring(line.lastIndexOf(CommandChainInterpreter.OPERATION_OR));

			line = line.substring(CommandChainInterpreter.OPERATION_OR.length());
		}

		if(line.contains(" ")){
			line = line.substring(line.lastIndexOf(" "));
		}

		return line;
	}

	private boolean startCandidateWithPrefix(FilterContext context) {
		return startsWithPrefix(context.getCandidate());
	}

	private boolean startsWithPrefix(String string) {
		for(String prefix : prefixes){
			if(string.startsWith(prefix)){
				return true;
			}
		}

		return false;
	}
}
