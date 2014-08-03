package de.raysha.lib.jsimpleshell.completer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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
	public static class Type {
		/**
		 * If the parameter type is DIRECTORY_ONLY only directories will be completed.
		 */
		public static final String DIRECTORY_ONLY = "java.io.file_dirOnly";
	}
	
	private static final Set<String> RESPONSIBLE_FOR = Collections.unmodifiableSet(new HashSet<String>(){{
		add(File.class.getName());
		add(Type.DIRECTORY_ONLY);
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
		
		if(Type.DIRECTORY_ONLY.equals(spec.getType())){
			filterDirectories(result);
		}
		
		return result;
	}

	private void filterDirectories(Set<String> result) {
		Iterator<String> iter = result.iterator();
		while(iter.hasNext()){
			String path = iter.next();
			
			if(!new File(path).isDirectory()){
				iter.remove();
			}
		}
	}

	private boolean responsibleFor(ShellCommandParamSpec spec) {
		return 	RESPONSIBLE_FOR.contains(spec.getValueClass().getName()) ||
				RESPONSIBLE_FOR.contains(spec.getType());
	}

}
