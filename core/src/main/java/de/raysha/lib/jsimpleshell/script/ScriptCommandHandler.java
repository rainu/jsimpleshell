package de.raysha.lib.jsimpleshell.script;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.ShellCommand;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.handler.CommandHookDependent;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;

/**
 * This command handler contains all commands that are responsible for scripting.
 *
 * @author rainu
 */
public class ScriptCommandHandler implements CommandHookDependent {

	public static final String RETURN_VALUE_VARIABLE_NAME = "?";
	public static final String RETURN_STATUS_VARIABLE_NAME = "??";

	public static final Integer STATUS_CODE_SUCCESSFUL = 0;
	public static final Integer STATUS_CODE_UNSUCCESSFUL = 1;
	public static final Integer STATUS_CODE_FORBIDDEN = 2;

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
	public void showVariableDetails(
			@Param(value = "param.name.showvar", description = "param.description.showvar",
					type = Param.DefaultTypes.VARIABLE)
			String name){

		final Variable var = environment.getVariable(name);
		if(var == null) return;

		String type = null;

		if(var.isGlobal()){
			type = messageResolver.resolveGeneralMessage("message.showvar.global");
		}else{
			type = messageResolver.resolveGeneralMessage("message.showvar.local");
		}

		String out = messageResolver.resolveGeneralMessage("message.showvar.details")
			.replace("{name}", name)
			.replace("{value}", String.valueOf(getOutputValue(var)))
			.replace("{value-type}", var.getValue() != null ? var.getValue().getClass().getName() : "-")
			.replace("{type}", type);

		output.out().normal(out).println();
	}

	@Override
	public void cliBeforeCommand(ShellCommand command, Object[] parameter) {
		// do nothing
	}

	@Override
	public void cliAfterCommand(ShellCommand command, Object[] parameter, ExecutionResult result) {
		if(result.wasExecutionSuccessful()){
			setGlobalVariable(RETURN_STATUS_VARIABLE_NAME, STATUS_CODE_SUCCESSFUL);
			setGlobalVariable(RETURN_VALUE_VARIABLE_NAME, result.getResult());
		}else{
			setGlobalVariable(RETURN_STATUS_VARIABLE_NAME, STATUS_CODE_UNSUCCESSFUL);
			setGlobalVariable(RETURN_VALUE_VARIABLE_NAME, result.getThrown());
		}
	}

	@Override
	public void cliDeniedCommand(ShellCommand command, Object[] parameter, AccessDecision decision) {
		setGlobalVariable(RETURN_STATUS_VARIABLE_NAME, STATUS_CODE_FORBIDDEN);
		setGlobalVariable(RETURN_VALUE_VARIABLE_NAME, decision);
	}
}
