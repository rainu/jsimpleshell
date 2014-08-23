package de.raysha.lib.jsimpleshell.completer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;

/**
 * This {@link CandidatesChooser} is responsible for auto complete Locale values.
 *
 * @author rainu
 */
public class LocaleCandidatesChooser implements CandidatesChooser {
	/**
	 * If the parameter type is LOCALE locales will be completed.
	 */
	public static final String LOCALE_TYPE = "java.util.Locale";

	@Override
	public Candidates chooseCandidates(ShellCommandParamSpec paramSpec, String part) {
		if(!responsibleFor(paramSpec)) return null;

		List<String> values = new ArrayList<String>();
		for(Locale l : Locale.getAvailableLocales()){
			if(l.getCountry() == null || l.getCountry().equals("")){
				values.add(l.getLanguage());
			}else{
				values.add(l.getLanguage() + "_" + l.getCountry());
			}
		}

		Collections.sort(values);

		Iterator<String> iter = values.iterator();
		while(iter.hasNext()){
			if(part != null && !iter.next().toLowerCase().startsWith(part.toLowerCase())){
				iter.remove();
			}
		}

		return new Candidates(values);
	}

	private boolean responsibleFor(ShellCommandParamSpec paramSpec) {
		return Locale.class == paramSpec.getValueClass() || LOCALE_TYPE.equals(paramSpec.getType());
	}
}
