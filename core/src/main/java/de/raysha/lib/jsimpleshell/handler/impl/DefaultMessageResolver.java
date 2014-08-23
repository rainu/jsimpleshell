package de.raysha.lib.jsimpleshell.handler.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
	private Map<Locale, ResourceBundle> resourceBundles = new HashMap<Locale, ResourceBundle>();

	private DefaultMessageResolver() {}

	public synchronized static DefaultMessageResolver getInstance() {
		if(instance == null) {
			instance = new DefaultMessageResolver();
		}

		return instance;
	}

	@Override
	public void setLocale(Locale locale) {
		super.setLocale(locale);

		ResourceBundle.clearCache();
	}

	public Locale getLocale(){
		return this.locale;
	}

	@Override
	protected String resolveMessage(String msg) {
		String resolved = msg;
		try{
			resolved = getResourceBundle(this.locale).getString(msg);
		}catch(Exception e){ }

		return resolved;
	}

	@Override
	public boolean supportsLocale(Locale locale) {
		if(Locale.ENGLISH.getLanguage().equals(locale.getLanguage())){
			return true;
		}
		if(Locale.GERMAN.getLanguage().equals(locale.getLanguage())){
			return true;
		}

		return false;
	}

	private synchronized ResourceBundle getResourceBundle(Locale locale) {
		if(!resourceBundles.containsKey(locale)) {
			try {
				ResourceBundle resourceBundle = ResourceBundle.getBundle("jss-default-messages", locale);
				resourceBundles.put(locale, resourceBundle);
			} catch (Exception e) {
				throw new IllegalStateException("Could not load resource bundle from classpath!", e);
			}
		}

		return resourceBundles.get(locale);
	}

}
