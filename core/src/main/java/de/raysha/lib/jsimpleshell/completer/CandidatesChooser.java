package de.raysha.lib.jsimpleshell.completer;

import java.util.Collection;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;
import de.raysha.lib.jsimpleshell.annotation.Param;

/**
 * This interface is responsible for choosing candidates for special {@link Param}eters.
 * 
 * @author rainu
 *
 */
public interface CandidatesChooser {

	/**
	 * This method will be called if should be completed.
	 * 
	 * @param paramSpec The parameter which is affected.
	 * @param part The already typed part of parameter.
	 * @return A Collection which contains all possible candidates.
	 */
	public Collection<String> chooseCandidates(ShellCommandParamSpec paramSpec, String part);
}
