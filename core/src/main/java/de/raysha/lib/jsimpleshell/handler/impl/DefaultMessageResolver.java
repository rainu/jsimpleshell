package de.raysha.lib.jsimpleshell.handler.impl;

import java.io.IOException;
import java.util.Properties;

import de.raysha.lib.jsimpleshell.handler.MessageResolver;

/**
 * This {@link MessageResolver} resolve the default messages. This resolver
 * should be used as fallback.
 * 
 * @author rainu
 */
public final class DefaultMessageResolver extends AbstractMessageResolver {
	private static DefaultMessageResolver instance;
	private Properties properties;
	
	private DefaultMessageResolver() {}
	
	public synchronized static DefaultMessageResolver getInstance() {
		if(instance == null) {
			instance = new DefaultMessageResolver();
		}
		
		return instance;
	}
	
	@Override
	protected String resolveMessage(String msg) {
		return getProperties().getProperty(msg, msg);
	}

	private synchronized Properties getProperties() {
		if(properties == null) {
			properties = new Properties();
			try {
				properties.load(getClass().getResourceAsStream("/default-messages.properties"));
			} catch (IOException e) {
				throw new IllegalStateException("Could not load default-messages.properties from classpath!", e);
			}
		}
		
		return properties;
	}

}
