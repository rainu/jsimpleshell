package de.raysha.lib.jsimpleshell;

import java.util.Arrays;

import de.raysha.lib.jsimpleshell.annotation.Command;

public class AmbiguousCommands {
	@Command
	public String set(Boolean value){
		return "Boolean: " + value;
	}
	
	@Command
	public String set(Integer value){
		return "Integer: " + value;
	}
	
	@Command
	public String set(String value){
		return "String: " + value;
	}
	
	@Command
	public String setVar(String...value){
		return "String[]: " + Arrays.toString(value);
	}
	
	@Command
	public String setVar(Integer...value){
		return "Integer[]: " + Arrays.toString(value);
	}
	
	@Command
	public String setVar(Boolean...value){
		return "Boolean[]: " + Arrays.toString(value);
	}
	
	@Command
	public String complex(Boolean value, String sValue){
		return "complex: " + value + ", " + sValue;
	}
	
	@Command
	public String complex(String sValue, Boolean value){
		return "complex: " + sValue + ", " + value;
	}
}