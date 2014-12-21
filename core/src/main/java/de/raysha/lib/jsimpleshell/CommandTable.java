/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package de.raysha.lib.jsimpleshell;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.CommandDefinition;
import de.raysha.lib.jsimpleshell.exception.CLIException;
import de.raysha.lib.jsimpleshell.exception.CommandNotFoundException;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.io.InputConversionEngine;

/**
 * Command table is responsible for managing a lot of ShellCommands and is like a dictionary,
 * because its main function is to return a command by name.
 *
 * @author ASG
 */
public class CommandTable {

	private MessageResolver messageResolver;
	private Map<ShellCommand, CommandDefinition> commandTable = new LinkedHashMap<ShellCommand, CommandDefinition>();
	private CommandNamer namer;

	public CommandTable(CommandNamer namer) {
		this.namer = namer;
	}

	public void setMessageResolver(MessageResolver messageResolver) {
		this.messageResolver = messageResolver;
	}

	public CommandNamer getNamer() {
		return namer;
	}

	public List<ShellCommand> getCommandTable() {
		return Collections.unmodifiableList(new ArrayList<ShellCommand>(commandTable.keySet()));
	}

	public void removeCommands(Object handler){
		Iterator<ShellCommand> iter = commandTable.keySet().iterator();
		while(iter.hasNext()){
			if(iter.next().getHandler() == handler){
				iter.remove();
			}
		}
	}

	public void addMethod(Method method, Object handler, String prefix) {
		Command annotation = method.getAnnotation(Command.class);
		assert method != null;

		String name = "".equals(annotation.value()) ? annotation.name() : annotation.value();
		String abbrev = annotation.abbrev();
		String desciption = annotation.description();
		String header = annotation.header();

		CommandDefinition definition = new CommandDefinition(
				handler, method,
				prefix, name, abbrev,
				desciption, header,
				annotation.displayResult(), annotation.startsSubshell());

		addCommand(definition);
	}

	public void addCommand(CommandDefinition definition){
		if(definition == null) {
			throw new NullPointerException("The definition must not be null!");
		}

		ShellCommand command = new ShellCommand(
				definition.getHandler(),
				definition.getMethod(),
				definition.getPrefix(),
				resolveName(definition.getName(), definition.getMethod()),
				messageResolver);

		command.setAbbreviation(resolveAbbrev(definition.getAbbrev(), definition.getMethod()));
		command.setDescription(resolveDescription(definition.getDescription(), definition.getMethod()));
		command.setHeader(resolveHeader(definition.getHeader(), definition.getMethod()));
		command.setDisplayResult(definition.getDisplayResult());
		command.setStartsSubshell(definition.getStartsSubshell());

		commandTable.put(command, definition);
	}

	private String resolveName(String name, Method method) {
		if(name == null || "".equals(name)){
			name = namer.nameCommand(method).commandName;
		}else{
			name = messageResolver.resolveCommandName(name, method);
		}

		return name;
	}

	private String resolveAbbrev(String abbrev, Method method) {
		if(abbrev == null || "".equals(abbrev)){
			CommandNamer.NamingInfo autoNames = namer.nameCommand(method);
			for (String curAbbrev : autoNames.possibleAbbreviations) {
				if (!doesCommandExist(abbrev + curAbbrev, method.getParameterTypes().length)) {
					abbrev = curAbbrev;
					break;
				}
			}
		}else{
			abbrev = messageResolver.resolveCommandAbbrev(abbrev, method);
		}

		return abbrev;
	}

	private String resolveDescription(String description, Method method) {
		return messageResolver.resolveCommandDescription(description, method);
	}

	private String resolveHeader(String header, Method method) {
		return messageResolver.resolveCommandHeader(header, method);
	}

	private boolean doesCommandExist(String commandName, int arity) {
		for (ShellCommand cmd : commandTable.keySet()) {
			if (cmd.canBeDenotedBy(commandName) && cmd.getArity() == arity) {
				return true;
			}
		}
		return false;
	}


	public List<ShellCommand> commandsByName(String discriminator) {
		List<ShellCommand> collectedTable = new ArrayList<ShellCommand>();
		// collection
		for (ShellCommand cs : commandTable.keySet()) {
			if (cs.canBeDenotedBy(discriminator)) {
				collectedTable.add(cs);
			}
		}
		return collectedTable;
	}

	public ShellCommand lookupCommand(String discriminator, List<Token> tokens, InputConversionEngine inputEngine) throws CLIException {
		List<ShellCommand> collectedTable = commandsByName(discriminator);
		// reduction
		List<ShellCommand> reducedTable = new ArrayList<ShellCommand>();
		for (ShellCommand cs : collectedTable) {
			if (cs.getMethod().getParameterTypes().length == parameterCount(tokens) ||
				(cs.getMethod().isVarArgs() && (cs.getMethod().getParameterTypes().length-1 <= parameterCount(tokens)))) {

				reducedTable.add(cs);
			}
		}
		// selection
		if (collectedTable.size() == 0) {
			throw new CommandNotFoundException(discriminator);
		} else if (reducedTable.size() == 0) {
			throw new CommandNotFoundException(discriminator, parameterCount(tokens), false);
		} else if (reducedTable.size() > 1) {
			ShellCommand resolved = lookupAmbiguous(tokens, reducedTable, inputEngine);
			if(resolved == null){
				throw new CommandNotFoundException(discriminator, parameterCount(tokens), true);
			}

			return resolved;
		} else {
			return reducedTable.get(0);
		}
	}

	protected int parameterCount(List<Token> tokens) {
		if(Token.isCustomizedParamOrder(tokens)){
			//each parameter has two tokens (--<param name> <param value>)
			return (tokens.size() - 1) / 2;
		}

		return tokens.size() - 1;
	}

	private ShellCommand lookupAmbiguous(List<Token> tokens, List<ShellCommand> reducedTable, InputConversionEngine inputEngine) {
		sortPossibleCommands(reducedTable);

		removeInvalid(tokens, reducedTable, inputEngine);
		if(reducedTable.size() == 1){
			return reducedTable.get(0);
		}

		removeWithString(reducedTable);
		if(reducedTable.size() == 1){
			return reducedTable.get(0);
		}

		return null;
	}

	private void removeWithString(List<ShellCommand> reducedTable) {

		Iterator<ShellCommand> iter = reducedTable.iterator();
		while(iter.hasNext()){
			ShellCommand cmd = iter.next();

			if(containsString(cmd.getMethod().getParameterTypes())){
				iter.remove();
			}
		}
	}

	private void removeInvalid(List<Token> tokens,
			List<ShellCommand> reducedTable, InputConversionEngine inputEngine) {

		Iterator<ShellCommand> iter = reducedTable.iterator();
		while(iter.hasNext()){
			ShellCommand cmd = iter.next();

			try{
				inputEngine.convertToParameters(tokens,
						cmd.getParamSpecs(),
						cmd.getMethod().getParameterTypes(),
						cmd.getMethod().isVarArgs());
			}catch(Exception e){
				iter.remove();
			}
		}

		for(Token token : tokens){
			if(token.getString().startsWith("--")){
				final String paramName = token.getString().substring(2);

				iter = reducedTable.iterator();
				while(iter.hasNext()){
					ShellCommand cmd = iter.next();
					boolean found = false;
					for(ShellCommandParamSpec spec : cmd.getParamSpecs()){
						if(spec.getName().equals(paramName)){
							found = true;
							break;
						}
					}

					if(!found){
						iter.remove();
					}
				}
			}
		}
	}

	private void sortPossibleCommands(List<ShellCommand> commands){
		Collections.sort(commands, new Comparator<ShellCommand>() {
			@Override
			public int compare(ShellCommand o1, ShellCommand o2) {
				Class[] types1 = o1.getMethod().getParameterTypes();
				Class[] types2 = o2.getMethod().getParameterTypes();

				if(!containsString(types1) && containsString(types2)){
					return -1;
				}else if(containsString(types1) && !containsString(types2)){
					return 1;
				}

				return 0;
			}
		});
	}

	private boolean containsString(Class[] types) {
		for(Class type : types){
			if(type == String.class || type == String[].class) return true;
		}
		return false;
	}

	/**
	 * Refresh all commands stored in this table. This is needed if the locale was changed
	 * This ensures that the name, description, etc. be read again.
	 */
	public void refreshCommands() {
		LinkedHashMap<ShellCommand, CommandDefinition> oldTable = new LinkedHashMap<ShellCommand, CommandDefinition>(commandTable);
		commandTable.clear();

		for(Entry<ShellCommand, CommandDefinition> cmd : oldTable.entrySet()){
			addCommand(cmd.getValue());
		}
	}
}
