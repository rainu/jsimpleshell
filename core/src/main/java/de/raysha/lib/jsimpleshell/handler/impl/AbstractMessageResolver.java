package de.raysha.lib.jsimpleshell.handler.impl;

import java.lang.reflect.Method;
import java.util.Locale;

import de.raysha.lib.jsimpleshell.handler.MessageResolver;

/**
 * This {@link MessageResolver} don't care about the message source.
 * This resolver handles all message the same.
 *
 * @author rainu
 */
public abstract class AbstractMessageResolver implements MessageResolver {

	protected Locale locale = Locale.getDefault();

	@Override
	public String resolveCommandDescription(String description, Method targetMethod) {
		return resolveMessage(description);
	}

	@Override
	public String resolveCommandName(String name, Method targetMethod) {
		return resolveMessage(name);
	}

	@Override
	public String resolveCommandAbbrev(String abbrev, Method targetMethod) {
		return resolveMessage(abbrev);
	}

	@Override
	public String resolveCommandHeader(String header, Method targetMethod) {
		return resolveMessage(header);
	}

	@Override
	public String resolveParamDescription(String description, Method targetMethod) {
		return resolveMessage(description);
	}

	@Override
	public String resolveParamName(String name, Method targetMethod) {
		return resolveMessage(name);
	}

	@Override
	public String resolveGeneralMessage(String message) {
		return resolveMessage(message);
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * This method will be called if any message should be resolved.
	 *
	 * @param msg The current message value.
	 * @return The resolved message. Otherwise the <b>given</b> message.
	 */
	protected abstract String resolveMessage(String msg);
}
