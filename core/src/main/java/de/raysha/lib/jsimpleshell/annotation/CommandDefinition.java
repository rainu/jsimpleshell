package de.raysha.lib.jsimpleshell.annotation;

import java.lang.reflect.Method;

/**
 * This class represents the model of a {@link Command}-Annotation.
 *
 * @author rainu
 */
public class CommandDefinition {
	private final Object handler;
	private final Method method;
	private final String prefix;
	private final String name;
	private final String abbrev;
	private final String description;
	private final String header;
	private final boolean displayResult;
	private final boolean startsSubshell;

	public CommandDefinition(Object handler, Method method, String prefix,
			String name, String abbrev, String description, String header,
			Boolean displayResult, Boolean startsSubshell) {

		if(handler == null) {
			throw new NullPointerException("The handler must not be null!");
		}
		if(method == null) {
			throw new NullPointerException("The method must not be null!");
		}
		if(prefix == null) {
			prefix = "";
		}
		if(name == null) {
			name = getDefaults().name();
		}
		if(abbrev == null) {
			abbrev = getDefaults().abbrev();
		}
		if(description == null) {
			description = getDefaults().description();
		}
		if(header == null) {
			header = getDefaults().header();
		}
		if(displayResult == null){
			displayResult = getDefaults().displayResult();
		}
		if(startsSubshell == null){
			startsSubshell = getDefaults().startsSubshell();
		}

		this.handler = handler;
		this.method = method;
		this.prefix = prefix;
		this.name = name;
		this.abbrev = abbrev;
		this.description = description;
		this.header = header;
		this.displayResult = displayResult;
		this.startsSubshell = startsSubshell;
	}

	public CommandDefinition(Object handler, Method method) {
		this(handler, method, null, null, null, null, null, null, null);
	}

	public CommandDefinition(Object handler, Method method, String name) {
		this(handler, method, null, name, null, null, null, null, null);
	}

	public CommandDefinition(Object handler, Method method, String name, String abbrev) {
		this(handler, method, null, name, abbrev, null, null, null, null);
	}

	public CommandDefinition(Object handler, Method method, String prefix, String name, String abbrev) {
		this(handler, method, prefix, name, abbrev, null, null, null, null);
	}

	public Object getHandler() {
		return handler;
	}

	public Method getMethod() {
		return method;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getName() {
		return name;
	}

	public String getAbbrev() {
		return abbrev;
	}

	public String getDescription() {
		return description;
	}

	public String getHeader() {
		return header;
	}

	public boolean getDisplayResult() {
		return displayResult;
	}

	public boolean getStartsSubshell() {
		return startsSubshell;
	}

	private final Command getDefaults(){
		try {
			return CommandDefinition.class.getDeclaredMethod("annotationWirt").getAnnotation(Command.class);
		} catch (Exception e) {
			throw new IllegalStateException("This exception should be never thrown!", e);
		}
	}

	@Command
	private final void annotationWirt(){ }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		CommandDefinition other = (CommandDefinition) obj;
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
		return "CommandDefinition [method=" + method + "]";
	}
}
