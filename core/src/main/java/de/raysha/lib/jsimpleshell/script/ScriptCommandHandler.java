package de.raysha.lib.jsimpleshell.script;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;

/**
 * This command handler contains all commands that are responsible for scripting.
 *
 * @author rainu
 */
public class ScriptCommandHandler {

	@Inject
	private Environment environment;

	@Inject
	private OutputBuilder output;

	@Inject
	private Shell shell;

	@Inject
	private MessageResolver messageResolver;

	@Command(abbrev = "command.abbrev.variable.local", description = "command.description.variable.local",
			header = "command.header.variable.local", name = "command.name.variable.local")
	public void setLocalVariable(
			@Param(value="param.name.variable.local", description="param.description.variable.local")
			String name,
			@Param(value="param.name.variable.local.1", description="param.description.variable.local.1")
			Object value){

		environment.setVariable(new Variable(name, value));
	}

	@Command(abbrev = "command.abbrev.variable.local", description = "command.description.variable.local",
			header = "command.header.variable.local", name = "command.name.variable.local")
	public void setLocalVariable(
			@Param(value="param.name.variable.local", description="param.description.variable.local")
			String name){

		environment.setVariable(new Variable(name, null));
	}

	@Command(abbrev = "command.abbrev.variable.global", description = "command.description.variable.global",
			header = "command.header.variable.global", name = "command.name.variable.global")
	public void setGlobalVariable(
			@Param(value="param.name.variable.global", description="param.description.variable.global")
			String name,
			@Param(value="param.name.variable.global.1", description="param.description.variable.global.1")
			Object value){

		environment.setVariable(new Variable(name, value, true));
	}

	@Command(abbrev = "command.abbrev.variable.global", description = "command.description.variable.global",
			header = "command.header.variable.global", name = "command.name.variable.global")
	public void setGlobalVariable(
			@Param(value="param.name.variable.global", description="param.description.variable.global")
			String name){

		environment.setVariable(new Variable(name, null, true));
	}

	@Command(abbrev = "command.abbrev.showenv", description = "command.description.showenv",
			header = "command.header.showenv", name = "command.name.showenv")
	public void showEnvironment(){
		for(Variable var : environment.getVariables()){
			output.out()
					.normal(var.getName())
					.normal("=")
					.normal(getOutputValue(var))
				.println();
		}
	}

	private Object getOutputValue(Variable var) {
		Object outputValue = var.getValue();
		try{
			outputValue = shell.getOutputConverter().convertOutput(var.getValue());
		}catch(Exception e){ }

		if(outputValue == var.getValue()){
			outputValue = String.valueOf(outputValue);
		}
		return outputValue;
	}

	@Command(abbrev = "command.abbrev.showvar", description = "command.description.showvar",
			header = "command.header.showvar", name = "command.name.showvar")
	public String showVariableDetails(
			@Param(value = "param.name.showvar", description = "param.description.showvar",
					type = Param.DefaultTypes.VARIABLE)
			String name){

		final Variable var = environment.getVariable(name);
		if(var == null) return null;

		String type = null;

		if(var.isGlobal()){
			type = messageResolver.resolveGeneralMessage("message.showvar.global");
		}else{
			type = messageResolver.resolveGeneralMessage("message.showvar.local");
		}

		return messageResolver.resolveGeneralMessage("message.showvar.details")
			.replace("{name}", name)
			.replace("{value}", String.valueOf(getOutputValue(var)))
			.replace("{value-type}", var.getValue() != null ? var.getValue().getClass().getName() : "-")
			.replace("{type}", type);


	}
}
