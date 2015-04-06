package de.raysha.lib.jsimpleshell.example;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import de.raysha.lib.jsimpleshell.annotation.Command;

public class Commands {

	//this command will be validated by the provided jsr-303 validator
	@Command
	public String percent(
			@Min(0)		//here we use the jsr-303 validation annotations
			@Max(100)	//here we use the jsr-303 validation annotations
			Integer percent){

		return "Your percent: " + percent + "%";
	}

	//this command will be validated by our CustomValidator!
	@Command
	public String myPercent(Integer percent){

		return "Your percent: " + percent + "%";
	}
}
