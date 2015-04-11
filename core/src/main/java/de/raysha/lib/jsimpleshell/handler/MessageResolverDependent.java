package de.raysha.lib.jsimpleshell.handler;

import de.raysha.lib.jsimpleshell.annotation.Inject;

/**
 * Classes that want to get the access of used {@link MessageResolver} should implement this interface.
 *
 * @deprecated Use the {@link Inject} annotation over a setter-method instead of using this interface
 * (it has the same effect).
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
