package de.raysha.lib.jsimpleshell.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jline.console.ConsoleReader;
import de.raysha.lib.jsimpleshell.PromptElement;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.exception.CommandNotFoundException;
import de.raysha.lib.jsimpleshell.exception.TokenException;
import de.raysha.lib.jsimpleshell.handler.CommandLoopObserver;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.handler.ShellManageable;
import de.raysha.lib.jsimpleshell.util.FileUtils;
import de.raysha.lib.jsimpleshell.util.PromptBuilder;

/**
 * This class uses a {@link ConsoleReader} for additional functionality.
 *
 * @author rainu
 *
 */
public class TerminalIO implements Input, Output, ShellManageable, CommandLoopObserver {
	public static final String MACRO_SUFFIX = ".jssm";
	private static final String PROMPT_SUFFIX = "> ";

	private ConsoleReader console;
	private PrintStream error;
	private BufferedReader scriptReader = null;
	private MessageResolver messageResolver;

	private File macroHome = new File("./");
	private File macroFile;
	private StringBuffer macroRecorder;

	private static enum InputState { USER, SCRIPT }
	private InputState inputState = InputState.USER;
	private Map<String, String> scriptParameters;

	private int lastCommandOffset = 0;

	public TerminalIO(ConsoleReader console) {
		this(console, null);
	}

	public TerminalIO(ConsoleReader console, OutputStream error) {
		this.console = console;
		this.error = error != null ? new PrintStream(error) : null;
	}

	public ConsoleReader getConsole() {
		return console;
	}

	@Override
	public void setMessageResolver(MessageResolver messageResolver) {
		this.messageResolver = messageResolver;
	}

	@Override
	public void output(Object obj, OutputConversionEngine oce) {
		if (obj == null) {
			return;
		} else {
			obj = oce.convertOutput(obj);
		}

		if (obj.getClass().isArray()) {
			int length = Array.getLength(obj);
			for (int i = 0; i < length; i++) {
				output(Array.get(obj, i), 0, oce);
			}
		} else if (obj instanceof Collection) {
			for (Object elem : (Collection) obj) {
				output(elem, 0, oce);
			}
		} else {
			output(obj, 0, oce);
		}
	}

	@Override
	public void outputException(String input, TokenException error) {
		int errIndex = error.getToken().getIndex() + lastCommandOffset;
		while (errIndex-- > 0) {
			printErr("-");
		}
		for (int i = 0; i < error.getToken().getString().length(); i++) {
			printErr("^");
		}
		printlnErr("");
		printlnErr(error);
	}

	@Override
	public void outputException(Throwable e) {
		if(e instanceof CommandNotFoundException){
			printlnErr(e.getMessage());
			return;
		}

		String stackTrace = extractStacktrace(e);
		printlnErr(stackTrace);
	}

	@Override
	public void outputHeader(String text) {
		if (text != null) {
			println(text);
		}
	}

	@Override
	public String readCommand(List<PromptElement> path) {
		try {
			String prompt = getPrompt(path);
			lastCommandOffset = prompt.length();

			if(inputState == InputState.SCRIPT){
				String command = readCommandFromScript(prompt);
				if (command != null) {
					return command;
				} else {
					closeScript();
				}
			}

			final String line = console.readLine(prompt);
			return line;
		} catch (IOException ex) {
			throw new Error(ex);
		}
	}

	private String readCommandFromScript(String prompt) throws IOException {
		String command = null;

		do{
			command = scriptReader.readLine();
			if(command != null){
				command = command.replaceAll("#.*$", "");
			}
		}while(command != null && "".equals(command.trim()));

		if (command != null) {
			for(Entry<String, String> scriptParam : scriptParameters.entrySet()){
				command = command.replaceAll("\\{" + scriptParam.getKey() + "\\}", scriptParam.getValue());
			}

			String completeLine = prompt + command;
			println(completeLine);
			lastCommandOffset = completeLine.length();
		}
		return command;
	}

	private void closeScript() throws IOException {
		if (scriptReader != null) {
			scriptReader.close();
			scriptReader = null;
		}
		inputState = InputState.USER;
	}

	@Override
	public void cliEnterLoop(Shell shell) {
		//nothing to do
	}

	@Override
	public void cliLeaveLoop(Shell shell) {
		if(inRecordMode()){
			try {
				FileUtils.write(macroFile, macroRecorder.toString(), false);
			} catch (IOException e) { }
		}
	}

	@Override
	public void cliBeforeCommandLine(String line) {
		try{
			recordLine(line);
		} catch (IOException ex) {
			throw new Error(ex);
		}
	}

	@Override
	public void cliAfterCommandLine(String line) {
		//do nothing
	}

	@Command(abbrev = "command.abbrev.runscript", description = "command.description.runscript",
			header = "command.header.runscript", name = "command.name.runscript")
	public void runScript(
			@Param(value="param.name.runscript", description="param.description.runscript",
					type = Param.DefaultTypes.FILE)
			String filename,
			@Param(value="param.name.runscript.1", description="param.description.runscript.1")
			String...parameters) throws FileNotFoundException {

		scriptReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		inputState = InputState.SCRIPT;

		Pattern parameterPattern = Pattern.compile("^(\\w{1,})=(.*)$");

		scriptParameters = new HashMap<String, String>();
		for(String p : parameters){
			Matcher m = parameterPattern.matcher(p);
			if(m.matches()){
				scriptParameters.put(m.group(1), m.group(2));
			}
		}
	}

	@Command(abbrev = "command.abbrev.listscriptarguments", description = "command.description.listscriptarguments",
			header = "command.header.listscriptarguments", name = "command.name.listscriptarguments")
	public Set<String> listScriptArguments(
			@Param(value="param.name.listscriptarguments", description="param.description.listscriptarguments",
					type = Param.DefaultTypes.FILE)
			String filename) throws IOException{

		BufferedReader reader = null;
		Pattern parameterPattern = Pattern.compile("\\{([^\\{\\}]*)\\}");
		TreeSet<String> arguments = new TreeSet<String>();

		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));

			String line = null;
			do{
				line = reader.readLine();
				if(line != null){
					Matcher m = parameterPattern.matcher(line);
					while(m.find()){
						arguments.add(m.group(1));
					}
				}
			}while(line != null);
		}finally{
			if(reader != null) reader.close();
		}

		return arguments;
	}

	@Command(abbrev = "command.abbrev.setmacrohome", description = "command.description.setmacrohome",
			header = "command.header.setmacrohome", name = "command.name.setmacrohome")
	public String setMacroHome(
			@Param(value = "param.name.setmacrohome", description = "param.description.setmacrohome",
					type = Param.DefaultTypes.DIRECTORY)
			File homeDir) {

		if(!homeDir.exists() || !homeDir.isDirectory()){
			return "message.macro.sethome.nodirectory";
		}

		this.macroHome = homeDir;

		return null;
	}

	@Command(abbrev = "command.abbrev.getmacrohome", description = "command.description.getmacrohome",
			header = "command.header.getmacrohome", name = "command.name.getmacrohome")
	public String getMacroHome() {
		return macroHome == null ? "" : macroHome.getPath();
	}

	@Command(abbrev = "command.abbrev.runmacro", description = "command.description.runmacro",
			header = "command.header.runmacro", name = "command.name.runmacro")
	public void runMacro(
			@Param(value = "param.name.runmacro", description = "param.description.runmacro",
					type = Param.DefaultTypes.MACRO_NAME)
			String name) throws IOException {

		runScript(new File(macroHome, name + MACRO_SUFFIX).getAbsolutePath());
	}

	@Command(abbrev = "command.abbrev.startrecord", description = "command.description.startrecord",
			header = "command.header.startrecord", name = "command.name.startrecord")
	public String startRecord(
			@Param(value = "param.name.startrecord", description = "param.description.startrecord")
			String name) throws IOException {

		if (inRecordMode()) {
			return "message.macro.record.alreadystarted";
		}

		this.macroFile = new File(macroHome, name + MACRO_SUFFIX);
		this.macroRecorder = new StringBuffer(getMacroHead());

		return "message.macro.record.start";
	}

	private String getMacroHead() {
		return (String)resolve("message.macro.record.head");
	}

	@Command(abbrev = "command.abbrev.stoprecord", description = "command.description.stoprecord",
			header = "command.header.stoprecord", name = "command.name.stoprecord")
	public String stopRecord() throws IOException{
		if (macroFile == null) {
			return "message.macro.record.notstarted";
		}

		//remove last line because this is the stop-record command
		macroRecorder.replace(macroRecorder.length() - 1, macroRecorder.length(), ""); //remove last new line
		macroRecorder.replace(macroRecorder.lastIndexOf("\n") + 1, macroRecorder.length(), "");

		try{
			FileUtils.write(macroFile, macroRecorder.toString(), false);
		}finally{
			this.macroFile = null;
			this.macroRecorder = null;
		}

		return "message.macro.record.stop";
	}

	private boolean inRecordMode() {
		return macroFile != null;
	}

	private void recordLine(String line) throws IOException {
		if(macroRecorder != null){
			macroRecorder.append(line);
			macroRecorder.append("\n");
		}
	}

	private String getPrompt(List<PromptElement> path) {
		return PromptBuilder.joinPromptElements(path, false, '/') + PROMPT_SUFFIX;
	}

	private void output(Object obj, int indent, OutputConversionEngine oce) {
		if (obj == null) {
			return;
		}

		if (obj != null) {
			obj = oce.convertOutput(obj);
		}

		for (int i = 0; i < indent; i++) {
			print("\t");
		}

		if (obj == null) {
			println("(null)");
		} else if (obj.getClass().isPrimitive() || obj instanceof String) {
			println(obj);
		} else if (obj.getClass().isArray()) {
			println("Array");
			int length = Array.getLength(obj);
			for (int i = 0; i < length; i++) {
				output(Array.get(obj, i), indent + 1, oce);
			}
		} else if (obj instanceof Collection) {
			println("Collection");
			for (Object elem : (Collection) obj) {
				output(elem, indent + 1, oce);
			}
		} else if (obj instanceof Throwable) {
			outputException((Throwable)obj);
		} else {
			println(obj);
		}
	}

	private String extractStacktrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);

		return sw.toString(); // stack trace as a string
	}

	public void printlnErr(Object o) {
		o = resolve(o);

		if (error == null) {
			println("[ERROR] " + o);
		} else {
			error.println(o);
			error.flush();
		}
	}

	public void printErr(Object o) {
		o = resolve(o);

		if (error == null) {
			print("[ERROR] " + o);
		} else {
			error.print(o);
			error.flush();
		}
	}

	public void println(Object o) {
		o = resolve(o);

		try {
			console.println(String.valueOf(o));
			console.flush();
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	public void print(Object o) {
		o = resolve(o);

		try {
			console.print(String.valueOf(o));
			console.flush();
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	private Object resolve(Object o){
		if(o instanceof String){
			return messageResolver.resolveGeneralMessage((String)o);
		}

		return o;
	}

}
