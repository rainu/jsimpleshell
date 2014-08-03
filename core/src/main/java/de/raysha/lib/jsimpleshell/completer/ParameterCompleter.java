package de.raysha.lib.jsimpleshell.completer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;
import de.raysha.lib.jsimpleshell.CommandTable;
import de.raysha.lib.jsimpleshell.ShellCommand;
import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;
import de.raysha.lib.jsimpleshell.Token;

/**
 * This {@link ParameterCompleter} is only active when the cursor is at an argument position after any command.
 * 
 * @author rainu
 */
public class ParameterCompleter implements Completer {
	private CommandTable commandTable;
	private CandidatesChooser candidatesChooser;
	
	private List<Token> token;
	private int paramIndex;
	private String buffer;
	private int cursor;
	private String paramPart;
	
	public ParameterCompleter(CommandTable cmdTable, CandidatesChooser candidatesChooser) {
		this.commandTable = cmdTable;
		this.candidatesChooser = candidatesChooser;
	}
	
	@Override
	public synchronized int complete(String buffer, int cursor, List<CharSequence> candidates) {
		this.buffer = buffer;
		this.cursor = cursor;
		this.token = Token.tokenize(buffer);
		this.paramIndex = getParamIndex(token, cursor);
		
		List<ShellCommandParamSpec> possibleParameters = getPossibleCommandParams();
		if(possibleParameters.isEmpty()) return -1;

		Set<String> choosenCandidates = new HashSet<String>();
		for(ShellCommandParamSpec spec : possibleParameters){
			Collection<String> c = candidatesChooser.chooseCandidates(spec, paramPart);
			choosenCandidates.addAll(c);
		}
		
		return complete(candidates, choosenCandidates);
	}

	private int complete(List<CharSequence> candidates, Set<String> choosenCandidates) {
		int startIndex = cursor;
		for(int i = cursor - 1; i >= 0; i--){
			if(buffer.charAt(i) == ' '){
				startIndex = i;
				break;
			}
		}
		
		int result = new StringsCompleter(choosenCandidates).complete(paramPart, cursor - startIndex, candidates);
		result += startIndex + 1;
		
		return result;
	}

	private List<ShellCommandParamSpec> getPossibleCommandParams() {
		List<ShellCommandParamSpec> possibleParameters = new ArrayList<ShellCommandParamSpec>();
		
		if(token.isEmpty()) return possibleParameters;
		if(token.size() == 1 && !buffer.matches("\\s*[^\\s]{1,}\\s{1,}$")) return possibleParameters;
		
		if(paramIndex < 0){
			if(cursor == buffer.length()){
				paramIndex = 0;
			}else{
				return possibleParameters;
			}
		}
		
		final String cmdName = token.get(0).getString();
		
		for(ShellCommand cmd : commandTable.getCommandTable()){
			if(cmdName.equals(cmd.getAbbreviation()) || cmdName.equals(cmd.getName())){
				if(paramIndex < cmd.getParamSpecs().length){
					possibleParameters.add(cmd.getParamSpecs()[paramIndex]);
				}
			}
		}
		
		if(paramIndex + 1 >= token.size()) paramPart = "";
		else paramPart = token.get(paramIndex + 1).getString();
		
		return possibleParameters;
	}

	private int getParamIndex(List<Token> token, int cursor) {
		int index = 0;
		
		for(int i=1; i < token.size(); i++){
			Token t = token.get(i);
			
			if(cursor > t.getIndex()){
				index = i;
			}
		}
		
		if(cursor -1 < buffer.length() && Character.isWhitespace(buffer.charAt(cursor - 1))){
			index++;
		}
	
		return index - 1;	//tokenizer begins with command
	}

}