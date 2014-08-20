package de.raysha.lib.jsimpleshell.completer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jline.console.completer.FileNameCompleter;
import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;

/**
 * This {@link CandidatesChooser} is responsible for completing file pathes.
 *
 * @author rainu
 *
 */
public class FileCandidatesChooser implements CandidatesChooser {
	/**
	 * If the parameter type is FILES_TYPE only directories and files will be completed.
	 */
	public static final String FILES_TYPE = "java.io.file_files";

	/**
	 * If the parameter type is DIRECTORY_ONLY_TYPE only directories will be completed.
	 */
	public static final String DIRECTORY_ONLY_TYPE = "java.io.file_dirOnly";

	private static final Set<String> RESPONSIBLE_FOR = Collections.unmodifiableSet(new HashSet<String>(){{
		add(File.class.getName());
		add(FILES_TYPE);
		add(DIRECTORY_ONLY_TYPE);
	}});

	private FileNameCompleter delegate = new FileNameCompleter();

	@Override
	public Candidates chooseCandidates(ShellCommandParamSpec spec, String part) {
		List<CharSequence> candidates = new ArrayList<CharSequence>();
		Set<String> result = new HashSet<String>();

		if(!responsibleFor(spec)) return new Candidates(result);

		final int index = delegate.complete(part, part.length(), candidates);

		for(CharSequence candidate : candidates){
			result.add(candidate.toString().trim());
		}

		if(DIRECTORY_ONLY_TYPE.equals(spec.getType())){
			filterDirectories(part.substring(0, index), result);
		}

		return new Candidates(result, index);
	}

	private void filterDirectories(String part, Set<String> result) {
		Iterator<String> iter = result.iterator();
		while(iter.hasNext()){
			String path = iter.next();

			if(!new File(part + path).isDirectory()){
				iter.remove();
			}
		}
	}

	protected boolean responsibleFor(ShellCommandParamSpec spec) {
		return 	RESPONSIBLE_FOR.contains(spec.getValueClass().getName()) ||
				RESPONSIBLE_FOR.contains(spec.getType());
	}

}
