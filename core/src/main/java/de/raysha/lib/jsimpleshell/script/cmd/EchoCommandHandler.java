package de.raysha.lib.jsimpleshell.script.cmd;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.raysha.lib.jsimpleshell.PromptElement;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.ExitException;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;
import de.raysha.lib.jsimpleshell.util.ColoredStringBuilder;
import de.raysha.lib.jsimpleshell.util.MessagePrompt;

public class EchoCommandHandler {

	@SuppressWarnings("serial")
	private static final Map<String, String> REPLACEMENTS = new HashMap<String, String>(){{
		put("\\a", "\u0007");
		put("\\b", "\b");
		put("\\e", "\u001B");
		put("\\f", "\f");
		put("\\n", "\n");
		put("\\r", "\r");
		put("\\t", "\t");
		put("\\v", "\u000B");
	}};

	private Pattern octalPattern = Pattern.compile("[^\\\\](\\\\0([0-7]{3}))");
	private Pattern hexPattern = Pattern.compile("[^\\\\](\\\\x([0-9A-Fa-f]{4}))");

	@Inject
	private Shell shell;

	@Inject
	private MessageResolver messageResolver;

	@Inject
	private OutputBuilder outputBuilder;

	@Command(abbrev = "command.abbrev.echo", description = "command.description.echo",
			header = "command.header.echo", name = "command.name.echo")
	public String echo(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		StringBuilder builder = new StringBuilder();

		for(Object value : values){
			builder.append(value);
		}

		String output = applyReplacements(builder);
		return output;
	}

	@Command(abbrev = "command.abbrev.echo.builder", description = "command.description.echo.builder",
			header = "command.header.echo.builder", name = "command.name.echo.builder", startsSubshell = true)
	public String buildEcho() throws IOException{
		ColoredStringBuilder builder = new ColoredStringBuilder();
		PromptElement prompt = new MessagePrompt("message.echo.prompt", messageResolver);

		Shell subShell = ShellBuilder.subshell(prompt, shell)
			.behavior()
				.addHandler(builder)
				.addHandler(new AdditionalBuilderCommands(builder))
				.disableExitCommand()
			.back().build();

		subShell.commandLoop();

		return echo(builder.build());
	}

	@Command(abbrev = "command.abbrev.echo.error", description = "command.description.echo.error",
			header = "command.header.echo.error", name = "command.name.echo.error")
	public void errorEcho(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		final String value = echo(values);

		outputBuilder.err()
			.normal(value)
		.println();
	}

	@Command(abbrev = "command.abbrev.echo.error.builder", description = "command.description.echo.error.builder",
			header = "command.header.echo.error.builder", name = "command.name.echo.error.builder", startsSubshell = true)
	public void buildErrorEcho() throws IOException{
		errorEcho(buildEcho());
	}

	String applyReplacements(StringBuilder builder) {
		boolean prepand = false;
		if(builder.length() >= 1 && builder.charAt(0) == '\\'){
			//our regexpr doesn't work if a special char is at the beginning!
			builder.insert(0, '_');
			prepand = true;
		}

		//\c
		if(builder.indexOf("\\c") != -1){
			builder.replace(builder.indexOf("\\c"), builder.length(), "");
		}

		for(Entry<String, String> replacement : REPLACEMENTS.entrySet()){
			Pattern p = Pattern.compile("[^\\\\](\\" + replacement.getKey() + ")");
			Matcher m = p.matcher(builder.toString());

			while(m.find()){
				builder.replace(m.start() + 1, m.end(), replacement.getValue());

				m = p.matcher(builder.toString());
			}
		}

		Matcher octal = octalPattern.matcher(builder.toString());
		while(octal.find()){
			String number = octal.group(2);

			builder.replace(octal.start() + 1, octal.end(), Character.toString((char)Integer.parseInt(number, 8)));
			octal = octalPattern.matcher(builder.toString());
		}

		Matcher hex = hexPattern.matcher(builder.toString());
		while(hex.find()){
			String number = hex.group(2);

			builder.replace(hex.start() + 1, hex.end(), Character.toString((char)Integer.parseInt(number, 16)));
			hex = hexPattern.matcher(builder.toString());
		}

		if(prepand){
			builder.replace(0, 1, "");
		}

		String result = builder.toString();
		result = result.replace("\\\\", "\\");

		return result;
	}

	public class AdditionalBuilderCommands {
		private final ColoredStringBuilder builder;

		public AdditionalBuilderCommands(ColoredStringBuilder builder) {
			this.builder = builder;
		}

		@Command(abbrev = "command.abbrev.echo.builder.show", description = "command.description.echo.builder.show",
				header = "command.header.echo.builder.show", name = "command.name.echo.builder.show")
		public String build(){
			String output =  applyReplacements(new StringBuilder(builder.build()));

			return output;
		}

		@Command(abbrev = "command.abbrev.echo.builder.exit", description = "command.description.echo.builder.exit",
				header = "command.header.echo.builder.exit", name = "command.name.echo.builder.exit")
		public void complete() throws ExitException {
			throw new ExitException();
		}
	}

}
