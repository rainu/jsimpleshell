package de.raysha.lib.jsimpleshell.completer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;
import jline.console.completer.FileNameCompleter;

/**
 * This {@link CandidatesChooser} is responsible for completing file pathes.
 * 
 * @author rainu
 *
 */
public class FileCandidatesChooser implements CandidatesChooser {
	private static final Set<String> RESPONSIBLE_FOR = Collections.unmodifiableSet(new HashSet<String>(){{
		add(File.class.getName());
	}});
	
	private FileNameCompleter delegate = new FileNameCompleter();
	
	@Override
	public Collection<String> chooseCandidates(ShellCommandParamSpec spec, String part) {
		if(!responsibleFor(spec)) return Collections.emptyList();
		
		List<CharSequence> candidates = new ArrayList<CharSequence>();
		
		delegate.complete(part, part.length(), candidates);
		
		Set<String> result = new HashSet<String>();
		for(CharSequence candidate : candidates){
			result.add(candidate.toString().trim());
		}
		
		return result;
	}

	private boolean responsibleFor(ShellCommandParamSpec spec) {
		return RESPONSIBLE_FOR.contains(spec.getValueClass().getName());
	}

}
