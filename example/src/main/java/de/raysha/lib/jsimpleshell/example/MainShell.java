package de.raysha.lib.jsimpleshell.example;

import java.io.IOException;

import de.raysha.lib.jsimpleshell.PromptElement;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.exception.ExitException;
import de.raysha.lib.jsimpleshell.handler.InputDependent;
import de.raysha.lib.jsimpleshell.handler.OutputDependent;
import de.raysha.lib.jsimpleshell.handler.ShellDependent;
import de.raysha.lib.jsimpleshell.io.InputBuilder;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;
import de.raysha.lib.jsimpleshell.util.ColoredStringBuilder;
import de.raysha.lib.jsimpleshell.util.PromptBuilder;

/**
 * This class contains all Commands for the main shell.
 *
 * <ul>
 *		<li>
 *			If you want that the shell make sub shell's you must implement the {@link ShellDependent} interface
 *			or annotate a field/method with {@link Inject} to get the parent shell that is needed if you want to create a subshell.
 *		</li>
 *		<li>
 *			If you want to make output within a command you must implement the {@link OutputDependent} interface
 *			or annotate a field/method with {@link Inject} to get the OutputBuilder. With that builder you can print output to out/err.
 *		</li>
 *		<li>
 *			If you want to get (user-)input directly within a command you must implement the {@link InputDependent} interface
 *			or annotate a field/method with {@link Inject} to get the InputBuilder. With that builder you have the possibility to read directly the input.
 *		</li>
 * </ul>
 *
 * @author rainu
 */
public class MainShell implements ShellDependent {
	private Shell shell;

	//field injection
	@Inject private OutputBuilder output;

	private InputBuilder input;

	@Override
	public void cliSetShell(Shell theShell) {
		this.shell = theShell;
	}

	//method injection
	@Inject
	public void setInput(InputBuilder input) {
		this.input = input;
	}

	@Command(abbrev = "e", description = "Print the argument to std-out.")
	//if you have not define a seperate name in @Command the method-name itself will be used as command name
	public String echo(
			@Param(value = "value", description = "The string which should be printed.")
			String...strings) {

		//the return value of a command-method will be printed out on std-out
		StringBuilder sb = new StringBuilder();
		if(strings != null) for(String s : strings){
			sb.append(s);
			sb.append(" ");
		}

		return sb.toString();
	}

	@Command(abbrev = "ce",
			name = "cecho", //the user must enter "cecho" to start this command
			description = "Start a new subshell. Here you can build a colored string that will printed out when you exit that shell.",
			//the header will always shown if the user run this command
			header = "Each subshell have his own commands. Use \"?list\" to show you which commands are available! Use \"exit\" to get out of this shell.")
	public void colorizedEcho() throws IOException {
		final ColorizedEcho echoBuilder = new ColorizedEcho();

		final String coloredPrompt = new ColoredStringBuilder().cyan("cecho").build();
		final PromptElement prompt = PromptBuilder.fromString(coloredPrompt);

		Shell subShell = ShellBuilder.subshell(prompt, shell)
							.addHandler(echoBuilder)
						.build();

		//the method will be blocked until the shell was abandoned
		subShell.commandLoop();

		//this command doesnt return the output but use the output directly
		output.out().normal(echoBuilder.build()).println();
	}

	@Command(abbrev = "ct", description = "Print colored text.")
	public void printColorText(){
		output.out().black("text").println();
		output.out().blue("text").println();
		output.out().cyan("text").println();
		output.out().green("text").println();
		output.out().magenta("text").println();
		output.out().red("text").println();
		output.out().white("text").println();
		output.out().yellow("text").println();

		output.out().blackBG("text").println();
		output.out().blueBG("text").println();
		output.out().cyanBG("text").println();
		output.out().greenBG("text").println();
		output.out().magentaBG("text").println();
		output.out().redBG("text").println();
		output.out().whiteBG("text").println();
		output.out().yellowBG("text").println();
	}

	@Command(abbrev = "sp", description = "Saves your secret password. I swear, in my hands it is save!")
	public String saveSecretPassword() throws IOException{
		return "So, your secret password is: " + input.invisibleIn().withPromt("Enter your password please: ").readLine() + " Thank you and good by... >:->";
	}

	@Command
	public void quit() throws ExitException{
		throw new ExitException("By by...");
	}
}
