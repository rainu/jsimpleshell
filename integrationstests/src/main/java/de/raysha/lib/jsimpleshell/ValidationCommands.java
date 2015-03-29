package de.raysha.lib.jsimpleshell;

import javax.validation.constraints.Pattern;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;

public class ValidationCommands {

	@Command
	public String validate(
			@Param("p1")
			@Pattern(regexp="[0-9]{1,}")
			String param){

		return "Parameter: " + param;
	}
}
