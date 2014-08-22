package de.raysha.lib.jsimpleshell;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;

public class ParamOrderCommands {

	@Command
	public String set(
			@Param(value="p1")
			String p1,
			@Param(value="p2")
			String p2,
			@Param(value="p3")
			String p3){
		return p1 + ", " + p2 + ", " + p3;
	}
}
