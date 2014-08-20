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
import java.util.List;

import de.raysha.lib.jsimpleshell.annotation.Command;
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
	private List<ShellCommand> commandTable = new ArrayList<ShellCommand>();
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
		return Collections.unmodifiableList(commandTable);
	}

	public void removeCommands(Object handler){
		Iterator<ShellCommand> iter = commandTable.iterator();
		while(iter.hasNext()){
			if(iter.next().getHandler() == handler){
				iter.remove();
			}
		}
	}

	public void addMethod(Method method, Object handler, String prefix) {
		Command annotation = method.getAnnotation(Command.class);
		assert method != null;

		String name = resolveName(annotation, method);
		String abbrev = resolveAbbrev(annotation, method);
		String desciption = resolveDescription(annotation, method);
		String header = resolveHeader(annotation, method);

		ShellCommand command = new ShellCommand(handler, method, prefix, name, messageResolver);

		command.setAbbreviation(abbrev);
		command.setDescription(desciption);
		command.setHeader(header);

		commandTable.add(command);

	}

	private String resolveName(Command annotation, Method method) {
		String name = "";

		if(annotation != null){
			name = messageResolver.resolveCommandName(annotation, method);
		}
		if(name == null || "".equals(name)){
			name = namer.nameCommand(method).commandName;
		}

		return name;
	}

	private String resolveAbbrev(Command annotation, Method method) {
		String abbrev = "";

		if(annotation != null){
			abbrev = messageResolver.resolveCommandAbbrev(annotation, method);
		}
		if(abbrev == null || "".equals(abbrev)){
			CommandNamer.NamingInfo autoNames = namer.nameCommand(method);
			for (String curAbbrev : autoNames.possibleAbbreviations) {
				if (!doesCommandExist(abbrev + curAbbrev, method.getParameterTypes().length)) {
					abbrev = curAbbrev;
					break;
				}
			}
		}

		return abbrev;
	}

	private String resolveDescription(Command annotation, Method method) {
		return messageResolver.resolveCommandDescription(annotation, method);
	}

	private String resolveHeader(Command annotation, Method method) {
		return messageResolver.resolveCommandHeader(annotation, method);
	}

	private boolean doesCommandExist(String commandName, int arity) {
		for (ShellCommand cmd : commandTable) {
			if (cmd.canBeDenotedBy(commandName) && cmd.getArity() == arity) {
				return true;
			}
		}
		return false;
	}


	public List<ShellCommand> commandsByName(String discriminator) {
		List<ShellCommand> collectedTable = new ArrayList<ShellCommand>();
		// collection
		for (ShellCommand cs : commandTable) {
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
			if (cs.getMethod().getParameterTypes().length == tokens.size()-1 ||
				(cs.getMethod().isVarArgs() && (cs.getMethod().getParameterTypes().length-1 <= tokens.size()-1))) {

				reducedTable.add(cs);
			}
		}
		// selection
		if (collectedTable.size() == 0) {
			throw new CommandNotFoundException(discriminator);
		} else if (reducedTable.size() == 0) {
			throw new CommandNotFoundException(discriminator, tokens.size()-1, false);
		} else if (reducedTable.size() > 1) {
			ShellCommand resolved = lookupAmbiguous(tokens, reducedTable, inputEngine);
			if(resolved == null){
				throw new CommandNotFoundException(discriminator, tokens.size()-1, true);
			}

			return resolved;
		} else {
			return reducedTable.get(0);
		}
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
						cmd.getMethod().getParameterTypes(),
						cmd.getMethod().isVarArgs());
			}catch(Exception e){
				iter.remove();
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
}
