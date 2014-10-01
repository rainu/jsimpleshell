package de.raysha.lib.jsimpleshell.script;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.annotation.Param;

/**
 * This command handler contains all commands that are responsible for scripting.
 *
 * @author rainu
 */
public class ScriptCommandHandler {

	@Inject
	private Environment environment;

	@Command(abbrev = "command.abbrev.variable.local", description = "command.description.variable.local",
			header = "command.header.variable.local", name = "command.name.variable.local")
	public void setLocalVariable(
			@Param(value="param.name.variable.local", description="param.description.variable.local")
			String name,
			@Param(value="param.name.variable.local.1", description="param.description.variable.local.1")
			Object value){

		System.out.println(name + "\t" + value);
	}

	@Command(abbrev = "command.abbrev.variable.global", description = "command.description.variable.global",
			header = "command.header.variable.global", name = "command.name.variable.global")
	public void setGlobalVariable(
			@Param(value="param.name.variable.global", description="param.description.variable.global")
			String name,
			@Param(value="param.name.variable.global.1", description="param.description.variable.global.1")
			Object value){

		System.out.println(name + "\t" + value);
	}
}
