package de.raysha.lib.jsimpleshell.completer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jline.console.completer.Completer;
import de.raysha.lib.jsimpleshell.CommandTable;
import de.raysha.lib.jsimpleshell.ShellCommand;
import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;
import de.raysha.lib.jsimpleshell.Token;
import de.raysha.lib.jsimpleshell.completer.CandidatesChooser.Candidates;

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
		if(buffer == null || buffer.isEmpty()) return -1;

		this.buffer = buffer;
		this.cursor = cursor;
		this.token = Token.tokenize(buffer);
		this.paramIndex = getParamIndex(token, cursor);

		List<String> possibleParameterNames = getPossibleParameterNames();
		if(possibleParameterNames != null && !possibleParameterNames.isEmpty()){
			return complete(candidates,
					Collections.singletonList(new Candidates(possibleParameterNames)));
		}

		List<ShellCommandParamSpec> possibleParameters = getPossibleCommandParams();
		if(possibleParameters.isEmpty()) return -1;

		List<Candidates> possibleCandidates = new ArrayList<Candidates>();
		for(ShellCommandParamSpec spec : possibleParameters){
			Candidates c = candidatesChooser.chooseCandidates(spec, paramPart);
			possibleCandidates.add(c);
		}

		return complete(candidates, possibleCandidates);
	}

	private int complete(List<CharSequence> candidates, List<Candidates> possibleCandidates) {
		Candidates reduced = reducePossibleCandiates(possibleCandidates);

		if(reduced.getValues().isEmpty()) return -1;

		int startIndex = cursor;
		for(int i = cursor - 1; i >= 0; i--){
			if(buffer.charAt(i) == ' '){
				startIndex = i;
				break;
			}
		}

		candidates.addAll(reduced.getValues());

		return startIndex + reduced.getIndex() + 1;
	}

	private Candidates reducePossibleCandiates(List<Candidates> possibleCandidates) {
		//collect all candidates with the highest index

		int maxIndex = 0;
		for(Candidates c : possibleCandidates){
			if(c.getIndex() > maxIndex){
				maxIndex = c.getIndex();
			}
		}

		Candidates reduced = new Candidates(new HashSet<String>(), maxIndex);
		for(Candidates c : possibleCandidates){
			if(c.getIndex() == maxIndex){
				reduced.getValues().addAll(c.getValues());
			}
		}

		return reduced;
	}

	private List<String> getPossibleParameterNames() {
		if(token.size() <= 1) return null;

		Token currentToken = null;
		if(paramIndex < 0){
			currentToken = token.get(1);
		}else if((paramIndex + 1) < token.size()){
			currentToken = token.get(paramIndex + 1);
		} else {
			boolean found = false;
			for(Token t : token){
				if(t.getString().startsWith("--")){
					found = true;
				}
			}

			if(!found) return null; //we are at the position of new parameter without "--" as prefix
		}

		final String cmdName = token.get(0).getString();
		final String prevValue = token.get(paramIndex).getString();

		final String tokenValue = currentToken == null ? "" : currentToken.getString();
		final String currentParameter = tokenValue.startsWith("--") ? tokenValue.substring(2) : "";

		List<String> alreadyUsed = new ArrayList<String>();
		for(int i=1; i < token.size(); i++){
			final String param = token.get(i).getString();

			if(param.startsWith("--") && !param.substring(2).equals(currentParameter)){
				alreadyUsed.add(param.substring(2));
			}
		}

		if(paramIndex % 2 == 0){
			if(!tokenValue.startsWith("--") && alreadyUsed.isEmpty()){
				return null;
			}
		}else{
			if(!tokenValue.startsWith("--") || prevValue.startsWith("--")){
				return null;
			}
		}

		List<String> candidates = new ArrayList<String>();

		cmdLoop: for(ShellCommand cmd : commandTable.getCommandTable()){
			if(	cmdName.equals(cmd.getPrefix() + cmd.getAbbreviation()) ||
				cmdName.equals(cmd.getPrefix() + cmd.getName())){

				List<ShellCommandParamSpec> availableSpecs =
						new ArrayList<ShellCommandParamSpec>(Arrays.asList(cmd.getParamSpecs()));

				for(String usedParam : alreadyUsed){
					boolean found = false;
					for(ShellCommandParamSpec spec : availableSpecs){
						if(usedParam.equals(spec.getName())){
							found = true;
							break;
						}
					}
					if(!found){
						//this command version is presumably not used
						continue cmdLoop;
					}
				}

				Iterator<ShellCommandParamSpec> iter = availableSpecs.iterator();
				while(iter.hasNext()){
					final ShellCommandParamSpec currentSpec = iter.next();

					if(!currentSpec.isVarArgs()){
						if(alreadyUsed.contains(currentSpec.getName())){
							iter.remove();
						} else if(!currentSpec.getName().startsWith(currentParameter)) {
							iter.remove();
						}
					}
				}

				if(!availableSpecs.isEmpty()){
					for(ShellCommandParamSpec spec : availableSpecs){
						String inputParam = "--" + spec.getName() + " ";
						candidates.add(inputParam);
					}
				}
			}
		}

		return candidates;
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
		final String prevParamToken = token.get(paramIndex).getString();

		for(ShellCommand cmd : commandTable.getCommandTable()){
			if(	cmdName.equals(cmd.getPrefix() + cmd.getAbbreviation()) ||
				cmdName.equals(cmd.getPrefix() + cmd.getName())){

				if(prevParamToken != null && prevParamToken.startsWith("--")){
					final String paramName = prevParamToken.substring(2);
					for(ShellCommandParamSpec spec : cmd.getParamSpecs()){
						if(spec.getName().equals(paramName)){
							possibleParameters.add(spec);
						}
					}
				}else if(paramIndex < cmd.getParamSpecs().length){
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
