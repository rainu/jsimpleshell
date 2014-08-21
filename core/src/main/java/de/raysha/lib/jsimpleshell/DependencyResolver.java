package de.raysha.lib.jsimpleshell;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import de.raysha.lib.jsimpleshell.annotation.Inject;
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
		resolveInterfaces(object);
		resolveAnnotations(object);
	}

	private void resolveInterfaces(Object object) {
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

	private void resolveAnnotations(Object object) {
		resolveFields(object);
		resolveMethods(object);
	}

	private void resolveFields(Object object) {
		Collection<Field> annotatedFields = collectAnnotatedFields(object);
		for(Field field : annotatedFields){
			resolveField(field, object);
		}
	}

	private Collection<Field> collectAnnotatedFields(Object object) {
		Set<Field> fields = new HashSet<Field>();

		for(Field field : object.getClass().getDeclaredFields()){
			if(isAnnotationPresent(field)){
				fields.add(field);
			}
		}

		return fields;
	}

	private void resolveField(Field field, Object object) {
		final boolean accessible = field.isAccessible();

		Object fieldValue = null;

		try{
			fieldValue = get(field.getType());
		}catch(NotResolvableDependencyException e){
			throw new NotResolvableDependencyException(object, field, null, e);
		}

		try{
			if(!accessible){
				field.setAccessible(true);
			}

			field.set(object, fieldValue);
		}catch(Exception e){
			throw new NotResolvableDependencyException(object, field, fieldValue, e);
		}finally{
			if(!accessible){
				field.setAccessible(false);
			}
		}
	}

	private void resolveMethods(Object object) {
		Collection<Method> annotatedMethods = collectAnnotatedMethods(object);
		for(Method method : annotatedMethods){
			resolveMethod(method, object);
		}
	}

	private Collection<Method> collectAnnotatedMethods(Object object) {
		Set<Method> methods = new HashSet<Method>();

		for(Method method : object.getClass().getDeclaredMethods()){
			if(isAnnotationPresent(method)){
				methods.add(method);
			}
		}

		return methods;
	}

	private void resolveMethod(Method method, Object object) {
		final boolean accessible = method.isAccessible();

		Object[] parameterValues = new Object[method.getParameterTypes().length];

		try{
			for(int i=0; i < parameterValues.length; i++){
				parameterValues[i] = get(method.getParameterTypes()[i]);
			}
		}catch(NotResolvableDependencyException e){
			throw new NotResolvableDependencyException(object, method, parameterValues, e);
		}

		try{
			if(!accessible){
				method.setAccessible(true);
			}

			method.invoke(object, parameterValues);
		}catch(Exception e){
			throw new NotResolvableDependencyException(object, method, parameterValues, e);
		}finally{
			if(!accessible){
				method.setAccessible(false);
			}
		}
	}

	private final static Set<String> injectClassNames = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
			Inject.class.getName(),	//own annotation
			"javax.inject.Inject",	//java ee injection annotation
			"org.springframework.beans.factory.annotation.Autowired" //spring own injection annotation
	)));

	private boolean isAnnotationPresent(AnnotatedElement annotatedElement) {
		for(Annotation a : annotatedElement.getAnnotations()){
			if(injectClassNames.contains(a.annotationType().getName())){
				return true;
			}
		}

		return false;
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
