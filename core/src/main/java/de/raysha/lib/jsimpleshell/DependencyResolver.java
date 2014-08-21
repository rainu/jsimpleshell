package de.raysha.lib.jsimpleshell;

import java.util.WeakHashMap;

import de.raysha.lib.jsimpleshell.exception.NotResolvableDependencyException;
import de.raysha.lib.jsimpleshell.handler.InputDependent;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.handler.MessageResolverDependent;
import de.raysha.lib.jsimpleshell.handler.OutputDependent;
import de.raysha.lib.jsimpleshell.handler.ShellDependent;
import de.raysha.lib.jsimpleshell.io.InputBuilder;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;

/**
 * This class is responsible for resolving dependencies.
 *
 * @author rainu
 *
 */
public class DependencyResolver {
	private final WeakHashMap<Class<?>, Object> context = new WeakHashMap<Class<?>, Object>();

	/**
	 * Put a value into my context. Only instances which are stored in my context
	 * can be injected!
	 *
	 * @param value The value which should be stored in my context.
	 */
	public void put(Object value) {
		if(value == null) return;

		context.put(value.getClass(), value);
	}

	/**
	 * Resolve the dependencies from the given object.
	 *
	 * @param object The object which dependencies should be resolved.
	 * @throws NotResolvableDependencyException if any dependency could not be resolved.
	 */
	public void resolveDependencies(Object object) {
		if (object instanceof ShellDependent) {
			((ShellDependent)object).cliSetShell(get(Shell.class));
		}
		if (object instanceof OutputDependent) {
			((OutputDependent) object).cliSetOutput(get(OutputBuilder.class));
		}
		if (object instanceof InputDependent) {
			((InputDependent) object).cliSetInput(get(InputBuilder.class));
		}
		if (object instanceof MessageResolverDependent) {
			((MessageResolverDependent) object).cliSetMessageResolver(get(MessageResolver.class));
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T get(Class<T> type) {
		for(Class<?> curClass : context.keySet()){
			if(type.isAssignableFrom(curClass)){
				return (T)context.get(curClass);
			}
		}

		throw new NotResolvableDependencyException(type);
	}
}
