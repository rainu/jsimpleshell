package de.raysha.lib.jsimpleshell.example;

import org.springframework.beans.factory.annotation.Autowired;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager;
import de.raysha.lib.jsimpleshell.handler.CommandValidator;
import de.raysha.lib.jsimpleshell.handler.InputConverter;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.handler.OutputConverter;
import de.raysha.lib.jsimpleshell.io.InputBuilder;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;
import de.raysha.lib.jsimpleshell.script.Environment;

public class Commands {

	@Inject	//you can use the JSS own annotation
	private Shell currentShell;

	@javax.inject.Inject //... or the default @Inject annotation from the javax-package (Java EE)
	private InputConverter inputConverter;

	@Autowired //... or the the spring own annoation
	private OutputConverter outputConverter;

	private InputBuilder input;

	private OutputBuilder output;

	private MessageResolver messageResolver;

	@Inject
	private CommandAccessManager accessManager;

	@Inject
	private CommandValidator validator;

	@Inject
	private Environment environment;

	@Inject	//you can also put the JSS own annotation on a "setter"-method (name of the method is not important!)
	public void setInput(InputBuilder input) {
		this.input = input;
	}

	@javax.inject.Inject //... and of course the java-ee annotaion works also
	public void setOutput(OutputBuilder output) {
		this.output = output;
	}

	@Autowired //... and the spring own annotation too
	public void setMessageResolver(MessageResolver messageResolver) {
		this.messageResolver = messageResolver;
	}

	@Command
	public String showDependencies(){
		StringBuilder builder = new StringBuilder();

		builder.append("Shell: ");
		builder.append(currentShell);
		builder.append("\nInputConverter: ");
		builder.append(inputConverter);
		builder.append("\nOutputConverter: ");
		builder.append(outputConverter);
		builder.append("\nInput: ");
		builder.append(input);
		builder.append("\nOutput: ");
		builder.append(output);
		builder.append("\nMessageResolver: ");
		builder.append(messageResolver);
		builder.append("\nAccessManager: ");
		builder.append(accessManager);
		builder.append("\nValidator: ");
		builder.append(validator);
		builder.append("\nEnvironment: ");
		builder.append(environment);

		return builder.toString();
	}
}
