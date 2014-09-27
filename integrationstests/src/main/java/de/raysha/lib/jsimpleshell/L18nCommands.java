package de.raysha.lib.jsimpleshell;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;

public class L18nCommands {
	@Inject OutputBuilder out;

	@Command(description = "{cmd.desc}")
	public void test(){

	}

	@Command
	public String message(){
		return "{general.message}";
	}

	@Command
	public void output(){
		out.out().normal("{general.message}").println();
	}
}