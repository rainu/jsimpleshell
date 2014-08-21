package de.raysha.lib.jsimpleshell.exception;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

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

	public NotResolvableDependencyException(Object object, Field field,
			Object fieldValue, Throwable cause) {
		super("Could not inject dependency(" + fieldValue.getClass().getName() +
				") into field(" + object.getClass().getName() + "[" + object +  "]." + field.getName() + ").",
			cause);
	}

	public NotResolvableDependencyException(Object object, Method method,
			Object[] parameterValues, Throwable cause) {
		super("Could not inject dependencies(" + Arrays.toString(parameterValues) +
				") into method(" + object.getClass().getName() + "[" + object +  "]" +  "." + method.getName() + ").",
			cause);
	}

}
