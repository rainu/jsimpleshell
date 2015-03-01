package de.raysha.lib.jsimpleshell.completer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.io.TerminalIO;

/**
 * This {@link CandidatesChooser} is responsible for choosing all available macros.
 *
 * @author rainu
 */
public class MacroNameCandidatesChooser extends AbstractCandidatesChooser {
	public static final String MACRO_NAME_TYPE = "de.raysha.lib.jsimpleshell.completer.MacroNameCandidatesChooser_macroName";

	@Inject private Shell shell;

	public MacroNameCandidatesChooser() {
		super(MACRO_NAME_TYPE);
	}

	@Override
	public Candidates chooseCandidates(ShellCommandParamSpec paramSpec, final String part) {
		if(!responsibleFor(paramSpec)) return null;

		String[] macros = shell.getMacroHome().list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.startsWith(part) && name.endsWith(TerminalIO.MACRO_SUFFIX)){
					return true;
				}
				return false;
			}
		});
		List<String> names = new ArrayList<String>();
		for(String macro : Arrays.asList(macros)){
			names.add(macro.replace(TerminalIO.MACRO_SUFFIX, ""));
		}

		Candidates candidates = new Candidates(names);
		return candidates;
	}
}
