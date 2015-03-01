package de.raysha.lib.jsimpleshell.handler.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.raysha.lib.jsimpleshell.handler.MessageResolver;

/**
 * This {@link MessageResolver} delegates all request to a list of
 * {@link MessageResolver}s.
 *
 * @author rainu
 */
public class CompositeMessageResolver implements MessageResolver {
	private final List<MessageResolver> resolverChain;
	private Locale locale;

	public CompositeMessageResolver() {
		this.resolverChain = new ArrayList<MessageResolver>();
	}

	public CompositeMessageResolver(List<MessageResolver> resolverChain){
		if(resolverChain == null){
			throw new NullPointerException("The chain must not be null!");
		}

		this.resolverChain = resolverChain;
	}

	public List<MessageResolver> getChain(){
		return resolverChain;
	}

	@Override
	public String resolveCommandDescription(String description, Method targetMethod) {
		String value = description;
		for(MessageResolver r : getChainWithDefault()){
			if(!r.supportsLocale(locale)) continue;

			String newValue = r.resolveCommandDescription(description, targetMethod);
			if(	(value == null && newValue != null) ||
				(value != null && newValue != null && !value.equals(newValue))){

				return newValue;
			}
		}
		return value;
	}

	@Override
	public String resolveCommandName(String name, Method targetMethod) {
		String value = name;
		for(MessageResolver r : getChainWithDefault()){
			if(!r.supportsLocale(locale)) continue;

			String newValue = r.resolveCommandName(name, targetMethod);
			if(	(value == null && newValue != null) ||
				(value != null && newValue != null && !value.equals(newValue))){

				return newValue;
			}
		}
		return value;
	}

	@Override
	public String resolveCommandAbbrev(String abbrev, Method targetMethod) {
		String value = abbrev;
		for(MessageResolver r : getChainWithDefault()){
			if(!r.supportsLocale(locale)) continue;

			String newValue = r.resolveCommandAbbrev(abbrev, targetMethod);
			if(	(value == null && newValue != null) ||
				(value != null && newValue != null && !value.equals(newValue))){

				return newValue;
			}
		}
		return value;
	}

	@Override
	public String resolveCommandHeader(String header, Method targetMethod) {
		String value = header;
		for(MessageResolver r : getChainWithDefault()){
			if(!r.supportsLocale(locale)) continue;

			String newValue = r.resolveCommandHeader(header, targetMethod);
			if(	(value == null && newValue != null) ||
				(value != null && newValue != null && !value.equals(newValue))){

				return newValue;
			}
		}
		return value;
	}

	@Override
	public String resolveParamDescription(String description, Method targetMethod) {
		String value = description;
		for(MessageResolver r : getChainWithDefault()){
			if(!r.supportsLocale(locale)) continue;

			String newValue = r.resolveParamDescription(description, targetMethod);
			if(	(value == null && newValue != null) ||
				(value != null && newValue != null && !value.equals(newValue))){

				return newValue;
			}
		}
		return value;
	}

	@Override
	public String resolveParamName(String name, Method targetMethod) {
		String value = name;
		for(MessageResolver r : getChainWithDefault()){
			if(!r.supportsLocale(locale)) continue;

			String newValue = r.resolveParamName(name, targetMethod);
			if(	(value == null && newValue != null) ||
				(value != null && newValue != null && !value.equals(newValue))){

				return newValue;
			}
		}
		return value;
	}

	@Override
	public String resolveGeneralMessage(String message) {
		String value = message;
		for(MessageResolver r : getChainWithDefault()){
			if(!r.supportsLocale(locale)) continue;

			String newValue = r.resolveGeneralMessage(message);
			if(	(value == null && newValue != null) ||
				(value != null && newValue != null && !value.equals(newValue))){

				return newValue;
			}
		}
		return value;
	}

	@Override
	public boolean supportsLocale(Locale locale) {
		for(MessageResolver r : getChainWithDefault()){
			if(r.supportsLocale(locale)){
				return true;
			}
		}

		return false;
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;

		for(MessageResolver r : getChainWithDefault()){
			r.setLocale(locale);
		}
	}

	private List<MessageResolver> getChainWithDefault() {
		List<MessageResolver> result = new ArrayList<MessageResolver>(resolverChain);
		result.add(DefaultMessageResolver.getInstance());

		return result;
	}
}
