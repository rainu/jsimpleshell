package de.raysha.lib.jsimpleshell.handler;

/**
 * Classes that want to get the access of used {@link MessageResolver} should implement this interface.
 *
 * @author rainu
 */
public interface MessageResolverDependent {

	/**
	 * This method pass a {@link MessageResolver}.
	 * 
	 * @param resolver The {@link MessageResolver} instance.
	 */
	public void cliSetMessageResolver(MessageResolver resolver);
}
