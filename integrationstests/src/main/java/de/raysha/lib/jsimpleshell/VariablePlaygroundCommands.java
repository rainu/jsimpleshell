package de.raysha.lib.jsimpleshell;

import de.raysha.lib.jsimpleshell.annotation.Command;

public class VariablePlaygroundCommands {

	@Command
	public Integer getInteger(){
		return 13;
	}

	@Command
	public String set(Integer value){
		return "Integer: " + value;
	}

	@Command
	public String set(String value){
		return setString(value);
	}

	@Command
	public String setString(String value){
		return "String: " + value;
	}

	@Command
	public String setAnything(Object value){
		return "Object: " + value;
	}
}
