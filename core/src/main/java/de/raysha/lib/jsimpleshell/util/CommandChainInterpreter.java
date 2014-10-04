package de.raysha.lib.jsimpleshell.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.raysha.lib.jsimpleshell.Token;

/**
 * This class is responsible for chaining commands. If a user enter multiple commands in one line
 * this class split it in multiple commands.
 *
 * @author rainu
 */
public class CommandChainInterpreter {
	public static final String OPERATION_AND = "&&";
	public static final String OPERATION_OR = ";";

	public static class CommandLine {
		private boolean and = false;
		private List<Token> tokens = new ArrayList<Token>();

		public boolean isAnd(){
			return and;
		}

		public boolean isOr(){
			return !and;
		}

		public List<Token> getTokens(){
			return tokens;
		}
	}

	/**
	 * Scan all tokens for concatenate operations and split tokens in separate commands.
	 *
	 * @param tokens All tokens from a user input line.
	 * @return A list of {@link CommandLine}. If the user typed on command in one line than
	 * one {@link CommandLine} will be contained in the returned list.
	 */
	public static List<CommandLine> interpretTokens(List<Token> tokens){
		if(tokens == null || tokens.isEmpty()) return Collections.emptyList();

		List<CommandLine> lines = new ArrayList<CommandLine>();
		CommandLine cmd = new CommandLine();
		lines.add(cmd);

		for(Token token : tokens){
			if(	token.getString().equals(OPERATION_AND) ||
				token.getString().equals(OPERATION_OR)){

				cmd = new CommandLine();
				lines.add(cmd);

				if(token.getString().equals(OPERATION_AND)){
					cmd.and = true;
				}else if(token.getString().equals(OPERATION_OR)){
					cmd.and = false;
				}


				continue;
			}

			cmd.tokens.add(token);
		}

		return lines;
	}
}
