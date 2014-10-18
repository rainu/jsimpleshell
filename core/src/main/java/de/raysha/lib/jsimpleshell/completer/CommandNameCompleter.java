package de.raysha.lib.jsimpleshell.completer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;
import de.raysha.lib.jsimpleshell.ShellCommand;
import de.raysha.lib.jsimpleshell.Token;
import de.raysha.lib.jsimpleshell.annotation.Param.DefaultTypes;
import de.raysha.lib.jsimpleshell.completer.filter.CandidateFilter;
import de.raysha.lib.jsimpleshell.completer.filter.CandidateFilter.FilterContext;
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
	private CandidateFilter filter;

	public CommandNameCompleter(CommandAccessManager accessManager, List<ShellCommand> commands) {
		this.accessManager = accessManager;
		this.commands = commands;
	}

	public void setFilter(CandidateFilter filter) {
		this.filter = filter;
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
			if(decision.getDecision() != Decision.DENIED){
				commandNames.add(cmd.getPrefix() + cmd.getName());
			}
		}

		filterCandidates(buffer, cursor, commandNames);

		return new StringsCompleter(commandNames).complete(cutBuffer, cursor, candidates) + firstTokenIndex;
	}

	private void filterCandidates(String part, int cursor, Collection<String> commandNames) {
		if(filter != null){
			Iterator<String> iter = commandNames.iterator();
			while(iter.hasNext()){
				FilterContext ctx = createFilterContext(part, cursor, iter.next());

				if(!filter.filter(ctx)){
					iter.remove();
				}
			}
		}
	}

	private FilterContext createFilterContext(String part, int cursor, String candidate) {
		return new FilterContext(cursor, part, DefaultTypes.COMMAND_NAME, candidate);
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
