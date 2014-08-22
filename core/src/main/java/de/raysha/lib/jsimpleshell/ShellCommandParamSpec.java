package de.raysha.lib.jsimpleshell;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;


/**
 * Specification of command's parameters, such as description given with Param annotation.
 */
public class ShellCommandParamSpec {

	static ShellCommandParamSpec[] forMethod(Method theMethod, MessageResolver msgResolver) {
		Class[] paramTypes = theMethod.getParameterTypes();
		ShellCommandParamSpec[] result = new ShellCommandParamSpec[theMethod.getParameterTypes().length];
		Annotation[][] annotations = theMethod.getParameterAnnotations();
		assert annotations.length == result.length;
		for (int i = 0; i < result.length; i++) {
			Param paramAnnotation = null;
			for (Annotation a : annotations[i]) {
				if (a instanceof Param) {
					paramAnnotation = (Param)a;
					break;
				}
			}
			if (paramAnnotation != null) {
				assert !paramAnnotation.value().isEmpty() : "@Param.value mustn\'t be empty";

				String name = resolveName(paramAnnotation, theMethod, msgResolver);
				String desc = resolveDescription(paramAnnotation, theMethod, msgResolver);
				String type = paramAnnotation.type();

				result[i] = new ShellCommandParamSpec(name, paramTypes[i], desc, i, type);
			} else {
				result[i] = new ShellCommandParamSpec(String.format("p%d", i + 1), paramTypes[i], "", i, "");
			}
		}
		return result;
	}

	private static String resolveName(Param annotation, Method method,
			MessageResolver msgResolver) {

		return msgResolver.resolveParamName(annotation, method);
	}

	private static String resolveDescription(Param annotation, Method method,
			MessageResolver msgResolver) {

		return msgResolver.resolveParamDescription(annotation, method);
	}

	private String name;
	private String description;
	private int position;
	private Class valueClass;
	private String type;

	public Class getValueClass() {
		return valueClass;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public int getPosition() {
		return position;
	}

	public String getType(){
		return type;
	}

	public ShellCommandParamSpec(String name, Class valueClass, String description, int position, String type) {
		super();
		this.name = name.replaceAll("\\s", "-");
		this.description = description;
		this.position = position;
		this.valueClass = valueClass;
		this.type = type;
	}
}
