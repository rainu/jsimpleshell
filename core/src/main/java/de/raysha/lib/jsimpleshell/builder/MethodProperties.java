package de.raysha.lib.jsimpleshell.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * This class represents a model that contains properties
 * for a {@link Method}.
 *
 * @author rainu
 */
public class MethodProperties {

	/**
	 * This interface represents a filter for methods.
	 *
	 * @author rainu
	 */
	public static interface Filter {
		/**
		 * Check if the given method matches.
		 *
		 * @param method The checked method.
		 * @return True if the method matched. Otherwise false.
		 */
		public boolean match(Method method);
	}

	Collection<Filter> filter = new HashSet<MethodProperties.Filter>();

	/**
	 * Add a {@link Modifier} that should be matched to a {@link Method}.
	 *
	 * @param mod The target {@link Modifier}. This is a value from the {@link Modifier}-Class.
	 * @return This {@link MethodProperties}
	 */
	public MethodProperties addModifier(final int mod){
		return addFilter(new Filter() {
			@Override
			public boolean match(Method method) {
				return (mod & method.getModifiers()) == mod;
			}
		});
	}

	/**
	 * Add a {@link Pattern} that have to be matched the {@link Method}-Name.
	 *
	 * @param pattern The {@link Pattern}
	 * @return This {@link MethodProperties}
	 */
	public MethodProperties addPattern(final Pattern pattern){
		return addFilter(new Filter() {
			@Override
			public boolean match(Method method) {
				return pattern.matcher(method.getName()).matches();
			}
		});
	}

	/**
	 * @see MethodProperties#addPattern(Pattern)
	 * @param regexp pattern The {@link Pattern}
	 * @return This {@link MethodProperties}
	 */
	public MethodProperties addPattern(String regexp){
		return addPattern(Pattern.compile(regexp));
	}

	/**
	 * Add a parent {@link Class}. Only {@link Method}s that are declared in this class
	 * will be matched.
	 *
	 * @param clazz The target parent {@link Class}.
	 * @return This {@link MethodProperties}
	 */
	public MethodProperties addParent(final Class<?> clazz){
		return addFilter(new Filter() {
			@Override
			public boolean match(Method method) {
				try {
					clazz.getMethod(method.getName(), method.getParameterTypes());
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		});
	}

	/**
	 * Add a "blacklist" {@link Class}. {@link Method}s that are declared in this class
	 * will be skipped.
	 *
	 * @param clazz The target blacklist {@link Class}.
	 * @return This {@link MethodProperties}
	 */
	public MethodProperties addBlacklistClass(final Class<?> clazz){
		return addFilter(new Filter() {
			@Override
			public boolean match(Method method) {
				return method.getDeclaringClass() != clazz;
			}
		});
	}

	/**
	 * Add a custom {@link Filter} that can be used to implement a custom filter-logic
	 * for {@link Method}s that should be matched.
	 *
	 * @param filter The {@link Filter}.
	 * @return This {@link MethodProperties}
	 */
	public MethodProperties addFilter(Filter filter){
		this.filter.add(filter);

		return this;
	}
}