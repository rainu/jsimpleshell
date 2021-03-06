package de.raysha.lib.jsimpleshell;

import java.util.Arrays;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;

public class ParamOrderCommands {

	@Command
	public String set(
			@Param(value="p1")
			String p1,
			@Param(value="p2")
			Boolean p2,
			@Param(value="p 3", type = Param.DefaultTypes.FILE)
			String p3){
		return p1 + ", " + p2 + ", " + p3;
	}

	@Command
	public String set(
			@Param(value="p1")
			String p1,
			@Param(value="opt")
			String...optional){

		return p1 + ", " + Arrays.toString(optional);
	}
}
