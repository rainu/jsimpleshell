package de.raysha.lib.jsimpleshell.builder;

import java.io.File;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import jline.console.ConsoleReader;
import de.raysha.lib.jsimpleshell.PromptElement;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.model.CommandDefinition;
import de.raysha.lib.jsimpleshell.util.ArrayHashMultiMap;
import de.raysha.lib.jsimpleshell.util.MultiMap;

class BuilderModel {
	private PromptElement prompt;
	private String appName = null;
	private final MultiMap<String, Object> auxHandlers = new ArrayHashMultiMap<String, Object>();
	private final Collection<Object> handlers = new LinkedList<Object>();
	private final Set<CommandDefinition> mainCommands = new HashSet<CommandDefinition>();
	private final Set<CommandDefinition> auxCommands = new HashSet<CommandDefinition>();
	private File history;
	private File macroHome;
	private Shell parent;
	private ConsoleReader console;
	private OutputStream error = System.err;
	private boolean useForeignConsole = false;
	private boolean fileNameCompleterEnabled = true;
	private boolean handleUserInterrupt = false;
	private boolean disableExit = false;
	private boolean colorOutput = true;


	PromptElement getPrompt() {
		return prompt;
	}
	void setPrompt(PromptElement prompt) {
		this.prompt = prompt;
	}
	String getAppName() {
		return appName;
	}
	void setAppName(String appName) {
		this.appName = appName;
	}
	MultiMap<String, Object> getAuxHandlers() {
		return auxHandlers;
	}
	Collection<CommandDefinition> getAuxCommands() {
		return auxCommands;
	}
	Collection<Object> getHandlers() {
		return handlers;
	}
	Collection<CommandDefinition> getMainCommands() {
		return mainCommands;
	}
	File getHistory() {
		return history;
	}
	void setHistory(File history) {
		this.history = history;
	}
	File getMacroHome() {
		return macroHome;
	}
	void setMacroHome(File macroHome) {
		this.macroHome = macroHome;
	}
	Shell getParent() {
		return parent;
	}
	void setParent(Shell parent) {
		this.parent = parent;
	}
	ConsoleReader getConsole() {
		return console;
	}
	void setConsole(ConsoleReader console) {
		this.console = console;
	}
	OutputStream getError() {
		return error;
	}
	void setError(OutputStream error) {
		this.error = error;
	}
	boolean isUseForeignConsole() {
		return useForeignConsole;
	}
	void setUseForeignConsole(boolean useForeignConsole) {
		this.useForeignConsole = useForeignConsole;
	}
	boolean isFileNameCompleterEnabled() {
		return fileNameCompleterEnabled;
	}
	void setFileNameCompleterEnabled(boolean fileNameCompleterEnabled) {
		this.fileNameCompleterEnabled = fileNameCompleterEnabled;
	}
	boolean isHandleUserInterrupt() {
		return handleUserInterrupt;
	}
	void setHandleUserInterrupt(boolean handleUserInterrupt) {
		this.handleUserInterrupt = handleUserInterrupt;
	}
	boolean isDisableExit() {
		return disableExit;
	}
	void setDisableExit(boolean disableExit) {
		this.disableExit = disableExit;
	}
	boolean isColorOutput() {
		return colorOutput;
	}
	void setColorOutput(boolean colorOutput) {
		this.colorOutput = colorOutput;
	}
}
