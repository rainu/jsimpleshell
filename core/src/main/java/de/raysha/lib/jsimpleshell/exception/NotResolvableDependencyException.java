package de.raysha.lib.jsimpleshell.exception;

/**
 * This exception will be thrown if an dependency could not be resolved.
 *
 * @author rainu
 *
 */
public class NotResolvableDependencyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotResolvableDependencyException(Class<?> type) {
		super("Could not resolve dependency for type '" + type.getCanonicalName() + "'");
	}

}
