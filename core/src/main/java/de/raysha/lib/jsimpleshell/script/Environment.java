package de.raysha.lib.jsimpleshell.script;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.raysha.lib.jsimpleshell.Shell;

/**
 * This class represents a environment of a {@link Shell}. The environment
 * contains things such like variables and so on.
 *
 * @author rainu
 */
public class Environment {
	private static final List<WeakReference<Environment>> allEnvironments = new ArrayList<WeakReference<Environment>>();

	private Map<String, Variable> variables = new HashMap<String, Variable>();

	public Environment() {
		allEnvironments.add(new WeakReference<Environment>(this));
	}

	/**
	 * Set a variable in this environment.
	 *
	 * @param var The variable instance.
	 */
	public void setVariable(Variable var){
		if(existsVariable(var.getName())) {
			//refresh content of variable
			Variable v = getVariable(var.getName());
			v.setValue(var.getValue());
		}else{
			//store this one
			variables.put(var.getName(), var);
		}

		if(var.isGlobal()){
			for(WeakReference<Environment> envRef : allEnvironments){
				if(envRef.get() == this) continue; //prevent endless loop

				envRef.get().setVariable(var);
			}
		}
	}

	/**
	 * Set a variable in this environment.
	 *
	 * @param name The name of the variable.
	 * @param value The value of the variable.
	 */
	public void setVariable(String name, Object value){
		setVariable(new Variable(name, value));
	}

	/**
	 * Get a variable from this environment. If a variable is not set <b>null</b>
	 * will be returned.
	 *
	 * @param name The name of the variable.
	 * @return The {@link Variable} if an variable was found under the given name.
	 * Or null if no {@link Variable} could be found.
	 */
	public Variable getVariable(String name){
		return variables.get(name);
	}

	/**
	 * Check if a variable is living in this environment.
	 *
	 * @param name The name of the variable.
	 * @return True if the variable exists. Otherwise false.
	 */
	public boolean existsVariable(String name){
		return variables.containsKey(name);
	}

	/**
	 * Get all {@link Variable}s in this {@link Environment}.
	 *
	 * @return The collections with {@link Variable}s.
	 */
	public Collection<Variable> getVariables() {
		return variables.values();
	}
}
