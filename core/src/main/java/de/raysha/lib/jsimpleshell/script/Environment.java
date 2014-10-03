package de.raysha.lib.jsimpleshell.script;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.handler.ShellManageable;

/**
 * This class represents a environment of a {@link Shell}. The environment
 * contains things such like variables and so on.
 *
 * @author rainu
 */
public class Environment implements ShellManageable {
	private static final List<Environment> allEnvironments = new ArrayList<Environment>();

	private Map<String, Variable> variables = new WeakHashMap<String, Variable>();

	public Environment() {
		allEnvironments.add(this);
	}

	@Override
	public void cliEnterLoop(Shell shell) {
		//do nothing
	}

	@Override
	public void cliLeaveLoop(Shell shell) {
		allEnvironments.remove(shell.getEnvironment());
	}

	/**
	 * Set a variable in this environment.
	 *
	 * @param var The variable instance.
	 */
	public void setVariable(Variable var){
		_setVariable(var);

		if(var.isGlobal()){
			for(Environment env : allEnvironments){
				if(env == this) continue; //prevent endless loop

				env._setVariable(var);
			}
		}
	}

	private void _setVariable(Variable var) {
		if(existsVariable(var.getName())) {
			//refresh content of variable
			Variable v = getVariable(var.getName());
			v.setValue(var.getValue());
		}else{
			//store this one
			variables.put(var.getName(), var);
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
