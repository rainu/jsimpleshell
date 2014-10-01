package de.raysha.lib.jsimpleshell.script;

/**
 * This class represents a {@link Environment}-Variable.
 *
 * @author rainu
 */
public class Variable {
	private final String name;
	private Object value;
	private boolean isGlobal;

	/**
	 * Creates a local empty variable.
	 *
	 * @param name The variable name.
	 */
	public Variable(String name){
		this(name, null);
	}

	/**
	 * Create a local variable.
	 *
	 * @param name The variable name.
	 * @param value The value of the variable.
	 */
	public Variable(String name, Object value){
		this(name, value, false);
	}

	/**
	 * Create a variable.
	 *
	 * @param name The variable name.
	 * @param value The value of the variable.
	 * @param isGlobal Is this variable global?
	 * That means that this variable is available in each environment.
	 */
	public Variable(String name, Object value, boolean isGlobal){
		if(name == null || name.trim().equals("")){
			throw new NullPointerException("The variable name must not be null or empty!");
		}

		this.name = name;
		this.value = value;
		this.isGlobal = isGlobal;
	}

	public String getName() {
		return name;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public boolean isGlobal() {
		return isGlobal;
	}

	public boolean isLocal(){
		return !isGlobal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Variable other = (Variable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Variable [name=" + name + ", value=" + value + ", global=" + isGlobal + "]";
	}
}
