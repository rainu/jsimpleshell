/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package de.raysha.lib.jsimpleshell;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision.Decision;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.Context;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.io.TerminalIO;

/**
 * Help command handler (usually prefixed by '?').
 * @author ASG
 */
public class HelpCommandHandler {

	@Inject private Shell owner;
	@Inject private MessageResolver messageResolver;
	@Inject private CommandAccessManager accessManager;

	@Command(abbrev = "command.abbrev.listall", description = "command.description.listall",
			header = "command.header.listall", name = "command.name.listall")
	public List<String> listAll() {
		List<ShellCommand> commands = owner.getCommandTable().getCommandTable();
		List<String> result = new ArrayList<String>(commands.size());
		for (ShellCommand command : commands) {
			AccessDecision decision = accessManager.checkCommandPermission(new Context(command));
			if(decision.getDecision() != Decision.DENIED){
				result.add(formatCommandShort(command));
			}
		}
		return result;
	}

	@Command(abbrev = "command.abbrev.list", description = "command.description.list",
			header = "command.header.list", name = "command.name.list")
	public List<String> list() {
		List<ShellCommand> commands = owner.getCommandTable().getCommandTable();
		List<String> result = new ArrayList<String>(commands.size());
		for (ShellCommand command : commands) {
			if (command.getPrefix() == null || command.getPrefix().isEmpty()) {
				AccessDecision decision = accessManager.checkCommandPermission(new Context(command));
				if(decision.getDecision() != Decision.DENIED){
					result.add(formatCommandShort(command));
				}
			}
		}
		return result;
	}

	@Command(abbrev = "command.abbrev.generatehelp", description = "command.description.generatehelp",
			header = "command.header.generatehelp", name = "command.name.generatehelp")
	public String generateHTMLHelp(
			@Param(value="param.name.generatehelp", description="param.description.generatehelp",
					type = Param.DefaultTypes.FILE)
			String fileName,
			@Param(value="param.name.generatehelp.1", description="param.description.generatehelp.1")
			boolean includePrefixed) throws IOException {

		final String HTML_FORMAT = "<html><head><title>Auto-generated command reference file</title></head>" +
				"<body>\n" +
				"<h1>%1$s Command Reference</h1>\n" +
				"<em>Auto-generated by the <a href=\"" + Info.getClicheHomepage() + "\">Cliche Shell</a></em>\n" +
				"%2$s</body></html>";

		List<ShellCommand> commands = owner.getCommandTable().getCommandTable();
		StringBuilder commandsHTML = new StringBuilder();
		for (ShellCommand command : commands) {
			if (command.getPrefix().equals("")) {
				appendCommandHTML(commandsHTML, command);
			}
		}
		if (includePrefixed) {
			for (ShellCommand command : commands) {
				if (!command.getPrefix().equals("")) {
					appendCommandHTML(commandsHTML, command);
				}
			}
		}

		String html = String.format(HTML_FORMAT, htmlEncode(owner.getAppName()), commandsHTML);

		File file = new File(fileName);
		OutputStreamWriter w = new FileWriter(file);
		try {
			w.write(html);
		} finally {
			w.close();
		}
		return String.format("Command table saved to %s", file.getAbsolutePath());
	}

	private static void appendCommandHTML(StringBuilder commandsHTML, ShellCommand command) {

		final String COMMAND_FORMAT = "<h2>%2$s <small>%3$s</small></h2>\n" +
				"<p><strong>abbrev:</strong> <big>%1$s</big></p>\n" +
				"<p>%4$s</p>\n" +
				"<table>\n" +
				"<tr><th>parameter</th><th>type</th><th>description</th></tr>\n" +
				"%5$s" +
				"</table>\n";
		final String PARAM_FORMAT = "<tr><td><strong>%1$s</strong></td><td>%2$s</td><td>%3$s</td></tr>\n";

		StringBuilder paramsHTML = new StringBuilder();
		ShellCommandParamSpec[] paramSpecs = command.getParamSpecs();
		for (ShellCommandParamSpec ps : paramSpecs) {
			paramsHTML.append(String.format(PARAM_FORMAT,
					htmlEncode(ps.getName()),
					htmlEncode(ps.getValueClass().getSimpleName()),
					htmlEncode(ps.getDescription())));
		}

		commandsHTML.append(String.format(COMMAND_FORMAT,
				htmlEncode(command.getPrefix() + command.getAbbreviation()),
				htmlEncode(command.getPrefix() + command.getName()),
				htmlEncode(formatCommandParamsShort(command)),
				htmlEncode(command.getDescription()),
				paramsHTML));
	}

	private static String htmlEncode(String s) {
		return s; // for now it's app developer's responsibility to ensure html-compatibility of the strings.
				  // Quick and dirty. But there's no htmlEncode in the JDK,
				  // and Jakarta Commons is no good in case of Cliche: there be no dependendencies!
	}

	@Command(abbrev = "command.abbrev.liststartwith", description = "command.description.liststartwith",
			header = "command.header.liststartwith", name = "command.name.liststartwith")
	public List<String> list(
			@Param(value="param.name.liststartwith", description="param.description.liststartwith") String startsWith) {

		List<ShellCommand> commands = owner.getCommandTable().getCommandTable();
		List<String> result = new ArrayList<String>(commands.size());
		for (ShellCommand command : commands) {
			if (command.startsWith(startsWith)) {
				AccessDecision decision = accessManager.checkCommandPermission(new Context(command));
				if(decision.getDecision() != Decision.DENIED){
					result.add(formatCommandShort(command));
				}
			}
		}
		return result;
	}

	@Command(abbrev = "command.abbrev.help", description = "command.description.help",
			header = "command.header.help", name = "command.name.help")
	public Object help() {
		String helpText = "";

		if(getTerminalWidth() >= 95){
			helpText += getBigLogo();
		}else if(getTerminalWidth() >= 60){
			helpText += getSmallLogo();
		}else{
			helpText += messageResolver.resolveGeneralMessage("message.help.logo.tiny");
		}

		helpText += messageResolver.resolveGeneralMessage("message.help.usage");

		return helpText.replace("{project.author}", String.valueOf(Info.getAuthor()))
				.replace("{cliche.homepage}", String.valueOf(Info.getClicheHomepage()))
				.replace("{jline2.homepage}", String.valueOf(Info.getJlineHomepage()))
				.replace("{project.homepage}", String.valueOf(Info.getProjectHomepage()))
				.replace("{project.version}", String.valueOf(Info.getVersion()));
	}

	private String getSmallLogo() {
		return messageResolver.resolveGeneralMessage("message.help.logo.small");
	}

	private String getBigLogo() {
		return messageResolver.resolveGeneralMessage("message.help.logo.big");
	}

	private int getTerminalWidth() {
		if(owner.getSettings().getInput() instanceof TerminalIO){
			return ((TerminalIO)owner.getSettings().getInput()).getConsole().getTerminal().getWidth();
		}

		return -1;
	}

	@Command(abbrev = "command.abbrev.helpdetail", description = "command.description.helpdetail",
			header = "command.header.helpdetail", name = "command.name.helpdetail")
	public Object help(
			@Param(value="param.name.helpdetail", description="param.description.helpdetail",
					type = Param.DefaultTypes.COMMAND_NAME)
			String commandName) {
		List<ShellCommand> commands = owner.getCommandTable().commandsByName(commandName);
		StringBuilder result = new StringBuilder();
		for (ShellCommand command : commands) {
			result.append(formatCommandLong(command, messageResolver));
			result.append("\n");
		}
		return result;
	}

	private static String formatCommandShort(ShellCommand command) {
		boolean hasAbbr = command.getAbbreviation() != null;
		return String.format("%s%s\t%s%s\t%s",
				hasAbbr ? command.getPrefix() : "",
				hasAbbr ? command.getAbbreviation() : "",
				command.getPrefix(),
				command.getName(),
				formatCommandParamsShort(command));
	}

	private static String formatCommandParamsShort(ShellCommand command) {
		ShellCommandParamSpec[] paramSpecs = command.getParamSpecs();
		StringBuilder result = new StringBuilder("(");

		boolean first = true;
		for (ShellCommandParamSpec paramSpec : paramSpecs) {
			if (!first) {
				result.append(", ");
			}
			result.append(paramSpec.getName());
			first = false;
		}
		if (command.getMethod().isVarArgs()) {
			result.append("...");
		}

		result.append(")");

		return result.toString();
	}

	private static String formatCommandLong(ShellCommand command, MessageResolver resolver) {
		final String cmd = resolver.resolveGeneralMessage("message.general.command");
		final String abbrev = resolver.resolveGeneralMessage("message.general.abbrev");
		final String params = resolver.resolveGeneralMessage("message.general.params");
		final String description = resolver.resolveGeneralMessage("message.general.description");
		final String none = resolver.resolveGeneralMessage("message.general.none");
		final String numberOfParam = resolver.resolveGeneralMessage("message.general.numberofparameters");
		final String paramVarArgs = resolver.resolveGeneralMessage("message.general.command.varargs");
		final String noParam = resolver.resolveGeneralMessage("message.general.command.noparam");

		StringBuilder sb = new StringBuilder(String.format(
				cmd + ": %s\n" +
				abbrev  + ":  %s\n" +
				params + ":  %s\n" +
				description + ": %s\n",
				command.getPrefix() + command.getName(),
				command.getAbbreviation() != null ? command.getPrefix() + command.getAbbreviation() : "(" + none + ")",
				formatCommandParamsShort(command),
				command.getDescription()));
		if (command.getArity() > 0) {
			sb.append(String.format(numberOfParam + ": %d\n", command.getArity()));
			Class[] paramTypes = command.getMethod().getParameterTypes();
			ShellCommandParamSpec[] paramSpecs = command.getParamSpecs();
			if (paramSpecs != null) {
				for (int i = 0; i < paramTypes.length; i++) {
					if (paramSpecs[i] != null) {
						sb.append(String.format("%s\t%s\t%s\n", paramSpecs[i].getName(), paramTypes[i].getSimpleName(),
								paramSpecs[i].getDescription()));
					}
				}
			}
			if (command.getMethod().isVarArgs()) {
				sb.append(paramVarArgs + "\n");
			}
		} else {
			sb.append(noParam + "\n");
		}
		return sb.toString();
	}
}
