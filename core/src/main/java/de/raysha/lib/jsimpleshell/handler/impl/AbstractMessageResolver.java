package de.raysha.lib.jsimpleshell.handler.impl;

import java.lang.reflect.Method;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;

/**
 * This {@link MessageResolver} don't care about the message source. 
 * This resolver handles all message the same.
 * 
 * @author rainu
 */
public abstract class AbstractMessageResolver implements MessageResolver {

	@Override
	public String resolveCommandDescription(Command command, Method annotatedMethod) {
		return resolveMessage(command.description());
	}

	@Override
	public String resolveCommandName(Command command, Method annotatedMethod) {
		return resolveMessage(command.name());
	}

	@Override
	public String resolveCommandAbbrev(Command command, Method annotatedMethod) {
		return resolveMessage(command.abbrev());
	}

	@Override
	public String resolveCommandHeader(Command command, Method annotatedMethod) {
		return resolveMessage(command.header());
	}

	@Override
	public String resolveParamDescription(Param param, Method annotatedMethod) {
		return resolveMessage(param.description());
	}

	@Override
	public String resolveParamName(Param param, Method annotatedMethod) {
		return resolveMessage(param.name());	
	}

	/**
	 * This method will be called if any message should be resolved.
	 * 
	 * @param msg The current message value.
	 * @return The resolved message. Otherwise the <b>given</b> message.
	 */
	protected abstract String resolveMessage(String msg);
}
