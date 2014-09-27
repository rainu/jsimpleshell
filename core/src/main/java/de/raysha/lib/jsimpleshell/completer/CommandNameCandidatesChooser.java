package de.raysha.lib.jsimpleshell.completer;

import java.util.HashSet;
import java.util.Set;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.ShellCommand;
import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;
import de.raysha.lib.jsimpleshell.annotation.Inject;

/**
 * This {@link CandidatesChooser} chooses the command names in the current shell.
 *
 * @author rainu
 *
 */
public class CommandNameCandidatesChooser implements CandidatesChooser {
	public static final String COMMAND_NAME_TYPE = "de.raysha.lib.jsimpleshell.Shell_commandName";

	@Inject private Shell shell;

	@Override
	public Candidates chooseCandidates(ShellCommandParamSpec paramSpec, String part) {
		if(!responsibleFor(paramSpec)) return null;

		Set<String> cmdSet = filterCommandNames(part);

		return new Candidates(cmdSet, 0);
	}

	private Set<String> filterCommandNames(String part) {
		Set<String> result = new HashSet<String>();

		for(ShellCommand cmd : shell.getCommandTable().getCommandTable()){
			String cmdLine = cmd.getPrefix() + cmd.getName();

			if(cmdLine.startsWith(part)){
				result.add(cmdLine);
			}
		}

		return result;
	}

	private boolean responsibleFor(ShellCommandParamSpec paramSpec) {
		return COMMAND_NAME_TYPE.equals(paramSpec.getType());
	}

}
