package de.raysha.lib.jsimpleshell.completer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;
import de.raysha.lib.jsimpleshell.handler.ShellDependent;
import de.raysha.lib.jsimpleshell.io.TerminalIO;

/**
 * This {@link CandidatesChooser} is responsible for choosing all available macros.
 * 
 * @author rainu
 */
public class MacroNameCandidatesChooser implements CandidatesChooser, ShellDependent {
	public static final String MACRO_NAME_TYPE = "de.raysha.lib.jsimpleshell.completer.MacroNameCandidatesChooser_macroName";
	
	private Shell shell;
	
	@Override
	public void cliSetShell(Shell theShell) {
		this.shell = theShell;
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

	private boolean responsibleFor(ShellCommandParamSpec paramSpec) {
		return MACRO_NAME_TYPE.equals(paramSpec.getType());
	}

}
