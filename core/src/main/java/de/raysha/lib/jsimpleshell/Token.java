/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package de.raysha.lib.jsimpleshell;

import java.util.ArrayList;
import java.util.List;


/**
 * Token associates index of a token in the input line with the token itself,
 * in order to be able to provide helpful error indecation (see below :)
 * ------------------------------------------------^ Misspelled word! (Exactly how it should work).
 *
 * This class is immutable.
 *
 * Parsing procedural module is also within.
 */
public class Token {

	private int index;
	private String string;

	public Token(int index, String string) {
		super();
		this.index = index;
		this.string = string;
	}

	public final int getIndex() {
		return index;
	}

	public final String getString() {
		return string;
	}

	@Override
	public String toString() {
		return (string != null ? string : "(null)") + ":" + Integer.toString(index);
	}

	@Override
	public boolean equals(Object obj) {
		// The contents generated by NetBeans.
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Token other = (Token)obj;
		if ((this.string == null) ? (other.string != null) : !this.string.equals(other.string)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 43 * hash + (this.string != null ? this.string.hashCode() : 0);
		return hash;
	}


	// *** Parser procmodule begins here ***

	/**
	 * State machine input string tokenizer.
	 *
	 * @param input String to be tokenized
	 * @return List of tokens
	 *
	 * @see de.raysha.lib.jsimpleshell.Token
	 * @see de.raysha.lib.jsimpleshell.Token#escapeString
	 */
	public static List<Token> tokenize(final String input) {
		List<Token> result = new ArrayList<Token>();
		if (input == null) {
			return result;
		}

		final int WHITESPACE = 0;
		final int WORD = 1;
		final int STRINGDQ = 2;
		final int STRINGSQ = 3;
		final int COMMENT = 4;
		final int ESCAPE = 5;

		int state = WHITESPACE;
		int lastState = state;

		char ch; // character in hand
		int tokenIndex = -1;
		StringBuilder token = new StringBuilder("");

		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			switch (state) {
				case WHITESPACE:
					if (Character.isWhitespace(ch)) {
						// keep state
					} else if (Character.isLetterOrDigit(ch) || ch == '_') {
						state = WORD;
						tokenIndex = i;
						token.append(ch);
					} else if (ch == '"') {
						state = STRINGDQ;
						tokenIndex = i;
					} else if (ch == '\'') {
						state = STRINGSQ;
						tokenIndex = i;
					} else if (ch == '#') {
						state = COMMENT;
					} else if (ch == '\\' && i < input.length()-1) {
						token.append(input.charAt(i+1));
						tokenIndex = i;
						i++;
					} else {
						state = WORD;
						tokenIndex = i;
						token.append(ch);
					}
					break;

				case WORD:
					if (Character.isWhitespace(ch)) {
						// submit token
						result.add(new Token(tokenIndex, token.toString()));
						token.setLength(0);
						state = WHITESPACE;
					} else if (Character.isLetterOrDigit(ch) || ch == '_') {
						token.append(ch); // and keep state
					} else if (ch == '"') {
						state = STRINGDQ; // but don't append; a"b"c is the same as abc.
					} else if (ch == '\'') {
						state = STRINGSQ; // but don't append; a"b"c is the same as abc.
					} else if (ch == '#') {
						// submit token
						result.add(new Token(tokenIndex, token.toString()));
						token.setLength(0);
						state = COMMENT;
					} else if (ch == '\\') {
						lastState = state;
						state = ESCAPE;
					} else {
						// for now we do allow special chars in words
						token.append(ch);
					}
					break;

				case STRINGDQ:
					if (ch == '"') {
						state = WORD;
					} else if (ch == '\\') {
						lastState = state;
						state = ESCAPE;
					} else {
						token.append(ch);
					}
					break;

				case STRINGSQ:
					if (ch == '\'') {
						state = WORD;
					} else if (ch == '\\') {
						lastState = state;
						state = ESCAPE;
					} else {
						token.append(ch);
					}
					break;

				case COMMENT:
					// eat ch
					break;

				case ESCAPE:
					token.append(ch);
					state = lastState;
					break;

				default:
					assert false : "Unknown state in Shell.tokenize() state machine";
					break;
			}
		}

		if (state == WORD || state == STRINGDQ || state == STRINGSQ || (state == WHITESPACE && !token.toString().trim().isEmpty())) {
			result.add(new Token(tokenIndex, token.toString()));
		}

		return result;
	}

	public static boolean isCustomizedParamOrder(List<Token> tokens) {
		if((tokens.size() - 1) % 2 != 0){
			//each parameter has two tokens (--<param name> <param value>)
			return false;
		}

		for(int i=1; i < tokens.size(); i += 2){
			if(!tokens.get(i).getString().startsWith("--")){
				return false;
			}
		}

		return true;
	}

	/**
	 * Escape given string so that tokenize(escapeString(str)).get(0).getString === str.
	 * @param input String to be escaped
	 * @return escaped string
	 */
	public static String escapeString(String input) {
		StringBuilder escaped = new StringBuilder(input.length() + 10);
		escaped.append('"');
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '"') {
				escaped.append("\\\"");
			} else if (input.charAt(i) == '\\') {
				escaped.append("\\\\");
			} else {
				escaped.append(input.charAt(i));
			}
		}
		escaped.append('"');
		return escaped.toString();
	}
}
