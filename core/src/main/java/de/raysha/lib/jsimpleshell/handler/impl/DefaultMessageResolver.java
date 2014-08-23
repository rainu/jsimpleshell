package de.raysha.lib.jsimpleshell.handler.impl;

import java.util.ResourceBundle;

import de.raysha.lib.jsimpleshell.handler.MessageResolver;

/**
 * This {@link MessageResolver} resolve the default messages. This resolver
 * should be used as fallback.
 *
 * @author rainu
 */
public final class DefaultMessageResolver extends AbstractMessageResolver {
	private static DefaultMessageResolver instance;
	private ResourceBundle resourceBundle;

	private DefaultMessageResolver() {}

	public synchronized static DefaultMessageResolver getInstance() {
		if(instance == null) {
			instance = new DefaultMessageResolver();
		}

		return instance;
	}

	@Override
	protected String resolveMessage(String msg) {
		String resolved = msg;
		try{
			resolved = getResourceBundle().getString(msg);
		}catch(Exception e){ }

		return resolved;
	}

	private synchronized ResourceBundle getResourceBundle() {
		if(resourceBundle == null) {
			try {
				resourceBundle = ResourceBundle.getBundle("jss-default-messages");
			} catch (Exception e) {
				throw new IllegalStateException("Could not load resource bundle from classpath!", e);
			}
		}

		return resourceBundle;
	}

}
