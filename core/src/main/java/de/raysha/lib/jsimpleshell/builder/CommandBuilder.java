package de.raysha.lib.jsimpleshell.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import de.raysha.lib.jsimpleshell.model.CommandDefinition;
import de.raysha.lib.jsimpleshell.builder.MethodProperties.Filter;

/**
 * This class contains methods which can generate list of {@link CommandDefinition}s.
 *
 * @author rainu
 */
public class CommandBuilder {

	/**
	 * Generate a command list with all public methods but the methods from the
	 * basic {@link Object}-Class.
	 *
	 * @param handler The handler object.
	 * @return The generated list of {@link CommandDefinition}s.
	 */
	public static List<CommandDefinition> allPublic(Object handler){
		return scan(handler,
			new MethodProperties()
				.addModifier(Modifier.PUBLIC)
				.addBlacklistClass(Object.class));
	}

	/**
	 * Generate a command list with all getter methods but the getter from
	 * the basic {@link Object}-Class.
	 *
	 * @param handler The handler object.
	 * @return The generated list of {@link CommandDefinition}s.
	 */
	public static List<CommandDefinition> allGetter(Object handler){
		return scan(handler,
			new MethodProperties()
				.addModifier(Modifier.PUBLIC)
				.addPattern("^(get|is)([A-Z].*|)$")
				.addBlacklistClass(Object.class));
	}

	/**
	 * Generate a command list with all setter methods but the getter from
	 * the basic {@link Object}-Class.
	 *
	 * @param handler The handler object.
	 * @return The generated list of {@link CommandDefinition}s.
	 */
	public static List<CommandDefinition> allSetter(Object handler){
		return scan(handler,
			new MethodProperties()
				.addModifier(Modifier.PUBLIC)
				.addPattern("^set([A-Z].*|)$")
				.addBlacklistClass(Object.class));
	}

	/**
	 * Generate a command list with all methods which are declared in the given class
	 * <b>and</b> which exists in the class of the given handler.
	 *
	 * @param handler The handler object.
	 * @param parentClass The target parent class.
	 * @return The generated list of {@link CommandDefinition}s.
	 */
	public static List<CommandDefinition> fromClass(Object handler, Class<?> parentClass){
		return scan(handler,
			new MethodProperties()
				.addModifier(Modifier.PUBLIC)
				.addParent(parentClass)
				.addBlacklistClass(Object.class));
	}

	/**
	 * Generate a command list with all methods but the methods from the
	 * basic {@link Object}-Class.
	 *
	 * @param handler The handler object.
	 * @return The generated list of {@link CommandDefinition}s.
	 */
	public static List<CommandDefinition> complete(Object handler){
		return scan(handler,
			new MethodProperties()
				.addBlacklistClass(Object.class));
	}

	/**
	 * Generate a command list with all methods which have the given {@link MethodProperties}.
	 *
	 * @param handler The handler object.
	 * @param properties The target {@link MethodProperties}
	 * @return The generated list of {@link CommandDefinition}s.
	 */
	public static List<CommandDefinition> scan(Object handler, MethodProperties properties){
		List<CommandDefinition> result = new LinkedList<CommandDefinition>();

		for (Class<?> clazz = handler.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
			for (Method method : clazz.getDeclaredMethods()) {
				if(match(method, properties)){
					CommandDefinition def = new CommandDefinition(handler, method, method.getName());

					result.add(def);
				}
			}
		}

		return result;
	}

	private static boolean match(Method method, MethodProperties properties) {
		for(Filter filter : properties.filter){
			if(!filter.match(method)){
				return false;
			}
		}

		return true;
	}
}
