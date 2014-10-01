package de.raysha.lib.jsimpleshell.completer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import de.raysha.lib.jsimpleshell.ShellCommand;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.Context;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision.Decision;
import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;

/**
 * This {@link Completer} is responsible for completing the command names (start of the line).
 *
 * @author rainu
 */
public class CommandNameCompleter implements Completer {

	private CommandAccessManager accessManager;
	private List<ShellCommand> commands;

	public CommandNameCompleter(CommandAccessManager accessManager, List<ShellCommand> commands) {
		this.accessManager = accessManager;
		this.commands = commands;
	}

	@Override
	public int complete(String buffer, int cursor, List<CharSequence> candidates) {
		Collection<String> commandNames = new HashSet<String>();
		for(ShellCommand cmd : commands){
			AccessDecision decision = accessManager.checkCommandPermission(new Context(cmd));
			if(decision.getDecision() == Decision.ALLOWED){
				commandNames.add(cmd.getPrefix() + cmd.getName());
			}
		}

		return new StringsCompleter(commandNames).complete(buffer, cursor, candidates);
	}

}
