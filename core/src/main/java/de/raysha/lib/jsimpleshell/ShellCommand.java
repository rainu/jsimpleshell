/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package de.raysha.lib.jsimpleshell;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.raysha.lib.jsimpleshell.exception.CLIException;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;


/**
 * Command table entry
 */
public class ShellCommand {

	private String prefix;
	private String name;
	private String description;
	private String abbreviation;
	private String header;
	private boolean displayResult = true;
	private boolean startsSubshell = false;
	private Method method;
	private Object handler;
	private ShellCommandParamSpec[] paramSpecs;

	public ShellCommand(Object handler, Method method, String prefix, String name, MessageResolver msgResolver) {
		super();
		assert method != null;
		this.paramSpecs = ShellCommandParamSpec.forMethod(method, msgResolver);
		assert paramSpecs.length == method.getParameterTypes().length;
		this.method = method;
		this.prefix = prefix;
		this.name = name;
		this.handler = handler;

		this.description = makeCommandDescription(method, paramSpecs);
	}

	private static String makeCommandDescription(Method method, ShellCommandParamSpec[] paramSpecs) {
		StringBuilder result = new StringBuilder();
		result.append(method.getName());
		result.append('(');
		Class[] paramTypes = method.getParameterTypes();
		assert paramTypes.length == paramSpecs.length;
		boolean first = true;
		for (int i = 0; i < paramTypes.length; i++) {
			if (!first) {
				result.append(", ");
			}
			first = false;
			if (paramSpecs[i] != null) {
				result.append(paramSpecs[i].getName());
				result.append(":");
				result.append(paramTypes[i].getSimpleName());
			} else {
				result.append(paramTypes[i].getSimpleName());
			}
		}
		result.append(") : ");
		result.append(method.getReturnType().getSimpleName());
		return result.toString();
	}

	public Object invoke(Object[] parameters)
			throws CLIException {
		assert method != null;
		try {
			Object result = method.invoke(handler, parameters);
			return result;
		} catch(InvocationTargetException ex){
			throw new CLIException(ex.getTargetException());
		} catch (Exception ex) {
			throw new CLIException(ex);
		}
	}

	public boolean canBeDenotedBy(String commandName) {
		return commandName.equals(prefix + name) || commandName.equals(prefix + abbreviation);
	}

	public int getArity() {
		return method.getParameterTypes().length;
	}

	public String getDescription() {
		return description;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getName() {
		return name;
	}

	public Method getMethod() {
		return method;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Object getHandler() {
		return handler;
	}

	public void setDisplayResult(boolean displayResult) {
		this.displayResult = displayResult;
	}

	public boolean isDisplayResult() {
		return displayResult;
	}

	public void setStartsSubshell(boolean startsSubshell) {
		this.startsSubshell = startsSubshell;
	}

	public boolean startsSubshell() {
		return startsSubshell;
	}

	public boolean startsWith(String prefix) {
		return (this.prefix + abbreviation).startsWith(prefix) ||
				(this.prefix + name).startsWith(prefix);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((abbreviation == null) ? 0 : abbreviation.hashCode());
		result = prime * result + ((handler == null) ? 0 : handler.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShellCommand other = (ShellCommand) obj;
		if (abbreviation == null) {
			if (other.abbreviation != null)
				return false;
		} else if (!abbreviation.equals(other.abbreviation))
			return false;
		if (handler == null) {
			if (other.handler != null)
				return false;
		} else if (!handler.equals(other.handler))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return prefix + name + "\t" + (abbreviation != null ? prefix + abbreviation : "") + "\t" +
				method.getParameterTypes().length + (method.isVarArgs() ? "+" : "") + "\t" + description;
	}

	public String getHeader() {
		return header;
	}

	public ShellCommandParamSpec[] getParamSpecs() {
		return paramSpecs;
	}
}
