package de.raysha.lib.jsimpleshell.completer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;
import de.raysha.lib.jsimpleshell.ShellCommand;
import de.raysha.lib.jsimpleshell.Token;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision.Decision;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.Context;

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
		List<Token> tokens = getCurrentTokens(buffer, cursor);

		int firstTokenIndex = 0;
		String cutBuffer = buffer;

		if(!tokens.isEmpty()){
			firstTokenIndex = tokens.get(0).getIndex();
			cutBuffer = buffer.substring(firstTokenIndex);
		}

		Collection<String> commandNames = new HashSet<String>();
		for(ShellCommand cmd : commands){
			AccessDecision decision = accessManager.checkCommandPermission(new Context(cmd));
			if(decision.getDecision() == Decision.ALLOWED){
				commandNames.add(cmd.getPrefix() + cmd.getName());
			}
		}

		return new StringsCompleter(commandNames).complete(cutBuffer, cursor, candidates) + firstTokenIndex;
	}

	private List<Token> getCurrentTokens(String buffer, int cursor) {
		List<Token> tokens = Token.tokenize(buffer);

		boolean beginToRemove = false;
		for(int i = tokens.size() - 1; i >= 0; i--){
			if(beginToRemove){
				tokens.remove(i);
				continue;
			}

			Token token = tokens.get(i);

			if(cursor >= token.getIndex()) {
				beginToRemove = true;
			}
		}

		return tokens;
	}
}
