package de.raysha.lib.jsimpleshell.builder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jline.console.ConsoleReader;
import jline.console.history.FileHistory;
import de.raysha.lib.jsimpleshell.CommandTable;
import de.raysha.lib.jsimpleshell.DashJoinedNamer;
import de.raysha.lib.jsimpleshell.HelpCommandHandler;
import de.raysha.lib.jsimpleshell.HistoryFlusher;
import de.raysha.lib.jsimpleshell.PromptElement;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.ShellSettings;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.completer.BooleanCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.CandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.CommandNameCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.EnumCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.FileCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.LocaleCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.MacroNameCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.VariableCandidatesChooser;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManagerDependent;
import de.raysha.lib.jsimpleshell.handler.CommandHookDependent;
import de.raysha.lib.jsimpleshell.handler.EnvironmentDependent;
import de.raysha.lib.jsimpleshell.handler.InputConverter;
import de.raysha.lib.jsimpleshell.handler.InputDependent;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.handler.MessageResolverDependent;
import de.raysha.lib.jsimpleshell.handler.OutputConverter;
import de.raysha.lib.jsimpleshell.handler.OutputDependent;
import de.raysha.lib.jsimpleshell.handler.ShellDependent;
import de.raysha.lib.jsimpleshell.io.TerminalIO;
import de.raysha.lib.jsimpleshell.script.Environment;
import de.raysha.lib.jsimpleshell.script.ScriptCommandHandler;
import de.raysha.lib.jsimpleshell.script.Variable;
import de.raysha.lib.jsimpleshell.script.VariableInputConverter;
import de.raysha.lib.jsimpleshell.util.ArrayHashMultiMap;
import de.raysha.lib.jsimpleshell.util.MultiMap;
import de.raysha.lib.jsimpleshell.util.PromptBuilder;

/**
 * This is a class that can be used to build a {@link Shell}.
 *
 * @author rainu
 */
public class ShellBuilder {
	private final BuilderModel model = new BuilderModel();

	private ShellBuilder(){ }

	/**
	 * Create a new {@link ShellBuilder} instance with which one you can build a {@link Shell}.
	 *
	 * @param prompt The prompt for this shell.
	 * @return A new instance of {@link ShellBuilder}
	 * @throws NullPointerException If the prompt is null.
	 */
	public static ShellBuilder shell(String prompt){
		return shell(PromptBuilder.fromString(prompt));
	}

	/**
	 * Create a new {@link ShellBuilder} instance with which one you can build a {@link Shell}.
	 *
	 * @param prompt The (complex) prompt for this shell.
	 * @return A new instance of {@link ShellBuilder}
	 * @throws NullPointerException If the prompt is null.
	 */
	public static ShellBuilder shell(PromptElement prompt){
		if(prompt == null){
			throw new NullPointerException("The prompt must not be null!");
		}

		ShellBuilder builder = new ShellBuilder();

		builder.model.setPrompt(prompt);

		return builder;
	}

	/**
	 * Create a new {@link ShellBuilder} instance with which one you can build a Sub{@link Shell}.
	 *
	 * @param subPrompt The sub-prompt for this sub shell.
	 * @param parent The parent {@link Shell} instance.
	 * @return A new instance of {@link ShellBuilder}
	 * @throws NullPointerException If the prompt is null.
	 */
	public static ShellBuilder subshell(String subPrompt, Shell parent){
		return subshell(PromptBuilder.fromString(subPrompt), parent);
	}

	/**
	 * Create a new {@link ShellBuilder} instance with which one you can build a Sub{@link Shell}.
	 *
	 * @param subPrompt The (complex) sub-prompt for this sub shell.
	 * @param parent The parent {@link Shell} instance.
	 * @return A new instance of {@link ShellBuilder}
	 * @throws NullPointerException If the prompt is null.
	 */
	public static ShellBuilder subshell(PromptElement subPrompt, Shell parent){
		if(subPrompt == null){
			throw new NullPointerException("The prompt must not be null!");
		}

		ShellBuilder builder = new ShellBuilder();

		builder.model.setPrompt(subPrompt);
		builder.model.setParent(parent);

		return builder;
	}

	/**
	 * Set the app name. This name will be shown if the shell will be entered.
	 *
	 * @param appName The app name.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setAppName(String appName) {
		model.setAppName(appName);
		return this;
	}

	/**
	 * Change the used {@link PromptElement}.
	 *
	 * @param prompt The {@link PromptElement} that should be used.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setPrompt(PromptElement prompt) {
		model.setPrompt(prompt);

		return this;
	}

	/**
	 * Change the used prompt.
	 *
	 * @param prompt The prompt that should be used.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setPrompt(String prompt) {
		model.setPrompt(PromptBuilder.fromString(prompt));

		return this;
	}

	/**
	 * Add a handler instance. The responsibility may be different:
	 * <ul>
	 * <li>Contains methods annotated with {@link Command} to provide commands</li>
	 * <li>Implements the {@link ShellDependent} interface to receive the {@link Shell} object (usefully for creating sub shells!)</li>
	 * <li>Implements the {@link InputConverter} interface to convert custom types from user input (string)</li>
	 * <li>Implements the {@link OutputConverter} interface to convert custom types to user-friendly-object (usually string)</li>
	 * <li>Implements the {@link OutputDependent} interface to get the possibility to print anything out</li>
	 * <li>Implements the {@link InputDependent} interface to get the possibility to read anything in</li>
	 * <li>Implements the {@link CommandHookDependent} interface to get the possibility to inform about command executions</li>
	 * <li>Implements the {@link CommandAccessManager} interface to get the possibility to allow/deny command executions</li>
	 * <li>Implements the {@link CommandAccessManagerDependent} interface to get access to the used {@link CommandAccessManager}</li>
	 * <li>Implements the {@link MessageResolver} interface to get the possibility to resolve {@link Command} / {@link Param}eter messages</li>
	 * <li>Implements the {@link MessageResolverDependent} interface to get access to the used {@link MessageResolver}</li>
	 * <li>Implements the {@link CandidatesChooser} interface to choose your own parameter candidates</li>
	 * <li>Implements the {@link EnvironmentDependent} interface to get access to the used {@link Environment}</li>
	 * </ul>
	 *
	 * @param handler A command handler.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder addHandler(Object handler) {
		model.getHandlers().add(handler);
		return this;
	}

	/**
	 * This method is very similar to addHandler, except ShellBuilder
	 * will pass all handlers registered with this method to all this shell's subshells.
	 *
	 * @see de.raysha.lib.jsimpleshell.builder.ShellBuilder#addHandler(java.lang.Object)
	 *
	 * @param handler Object which should be registered as handler.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder addAuxHandler(Object handler){
		model.getAuxHandlers().put("", handler);
		return this;
	}

	/**
	 * Set the {@link ConsoleReader} which you want to use.
	 *
	 * @param console The {@link ConsoleReader} instance.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setConsole(ConsoleReader console) {
		model.setConsole(console);
		model.setUseForeignConsole(true);

		return this;
	}

	/**
	 * Set Error {@link OutputStream} which will be used for printing error messages.
	 *
	 * @param error The {@link OutputStream} instance.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setError(OutputStream error) {
		model.setError(error);
		return this;
	}

	/**
	 * Create and use a new {@link ConsoleReader} with the specified
	 * Input- and Output- Stream.
	 *
	 * @param in The {@link InputStream} that should be used.
	 * @param out The {@link OutputStream} that should be used.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setConsole(InputStream in, OutputStream out) throws IOException {
		model.setConsole(new ConsoleReader(in, out));
		model.setUseForeignConsole(false);

		return this;
	}

	/**
	 * Should the shell handle user interrupts (CRTL-C)?
	 *
	 * @param handleUserInterrupt
	 *            False will cause that the JVM will handle SIGINT as normal, which usually
	 *            causes it to exit. True will cause that the JVM will not handle the signal.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setHandleUserInterrupt(boolean handleUserInterrupt) {
		model.setHandleUserInterrupt(handleUserInterrupt);

		return this;
	}

	/**
	 * Disable color output. No colored text will be printed even if the
	 * color-output-methods was called.
	 *
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder disableColor() {
		model.setColorOutput(false);

		return this;
	}

	/**
	 * Enable color output.
	 *
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder enableColor() {
		model.setColorOutput(true);

		return this;
	}

	/**
	 * Disable the file name completion mechanism!
	 *
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder disableFileNameCompleter(){
		model.setFileNameCompleterEnabled(false);

		return this;
	}

	/**
	 * Enable the file name completion mechanism!
	 *
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder enableFileNameCompleter(){
		model.setFileNameCompleterEnabled(true);

		return this;
	}

	/**
	 * Enable the exit command. By default it is enabled!
	 *
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder enableExitCommand(){
		model.setDisableExit(false);

		return this;
	}

	/**
	 * Disable the exit command. Be careful! You must implements
	 * your own exit mechanism (@see ExitException). If you do not,
	 * the user can never exit the shell normally!
	 *
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder disableExitCommand(){
		model.setDisableExit(true);

		return this;
	}

	/**
	 * Set the location of the history. By default the history will be not persisted!
	 *
	 * @param historyFile The location of the history. Null means that the history will not persisted.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setHistoryFile(File historyFile){
		model.setHistory(historyFile);

		return this;
	}

	/**
	 * Sets the directory where the macros are stored. This must be an <b>existing directory</b>!
	 *
	 * @param macroHome The new macro home directory.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setMacroHome(File macroHome){
		model.setMacroHome(macroHome);

		return this;
	}

	private void checkPrecondition() throws IOException {
		if(model.getConsole() == null){
			if(model.getParent() == null){
				model.setConsole(new ConsoleReader());
			}else if(model.getParent().getSettings().getInput() instanceof TerminalIO){
				model.setConsole(((TerminalIO)model.getParent().getSettings().getInput()).getConsole());
			}else{
				throw new IllegalStateException("The parent shell seams to be not built with ShellBuilder!");
			}
		}

		File macroHome = model.getMacroHome();
		if(macroHome != null && (!macroHome.exists() || !macroHome.isDirectory())){
			throw new IllegalArgumentException("The macro home must be an existing directory!");
		}
	}

	private void configure() {
		if(!model.isUseForeignConsole()){
			if(model.getHistory() != null && !(model.getConsole().getHistory() instanceof FileHistory)){
				try {
					model.getConsole().setHistory(new FileHistory(model.getHistory()));
				} catch (IOException e) {
					throw new RuntimeException("Could not configure file history.", e);
				}

				if(model.getAuxHandlers().get("historyFlusher").isEmpty()){
					model.getAuxHandlers().put("historyFlusher", new HistoryFlusher());
				}
			}

			model.getConsole().setHandleUserInterrupt(model.isHandleUserInterrupt());
		}
		model.getConsole().setExpandEvents(false);
	}

	private Shell buildShell() {
		TerminalIO io = new TerminalIO(model.getConsole(), model.getError());

		List<PromptElement> path = new ArrayList<PromptElement>(1);
		path.add(model.getPrompt());

		MultiMap<String, Object> modifAuxHandlers = new ArrayHashMultiMap<String, Object>(model.getAuxHandlers());
		modifAuxHandlers.put("!", io);

		Shell theShell = new Shell(new ShellSettings(io, io, modifAuxHandlers, false), model.getHandlers(),
				new CommandTable(new DashJoinedNamer(true)), path, buildInitialEnvironment());

		configureShell(theShell);
		return theShell;
	}

	private Environment buildInitialEnvironment() {
		return new Environment();
	}

	private Shell buildSubShell() {
		final Shell parent = model.getParent();

		List<PromptElement> newPath = new ArrayList<PromptElement>(parent.getPath());
		newPath.add(model.getPrompt());

		Shell subshell = new Shell(parent.getSettings().createWithAddedAuxHandlers(model.getAuxHandlers()), model.getHandlers(),
				new CommandTable(parent.getCommandTable().getNamer()), newPath, copyEnvironment(parent));

		configureShell(subshell);
		return subshell;
	}

	private Environment copyEnvironment(Shell shell) {
		Environment env = new Environment();

		for(Variable var : shell.getEnvironment().getVariables()){
			//transfer only global variables between shells
			if(var.isGlobal()){
				env.setVariable(var);
			}
		}

		return env;
	}

	private void configureShell(Shell shell) {
		shell.setAppName(model.getAppName());
		addDefaultHandler(shell);

		if(model.isFileNameCompleterEnabled()) {
			shell.addMainHandler(new FileCandidatesChooser(), "");
		}

		if(model.isDisableExit()){
			shell.disableExitCommand();
		}

		if(model.isColorOutput()){
			shell.enableColor();
		}else{
			shell.disableColor();
		}

		if(model.getMacroHome() != null){
			shell.setMacroHome(model.getMacroHome());
		}
	}

	private void addDefaultHandler(Shell shell) {
		shell.addMainHandler(shell, "!");
		shell.addMainHandler(new HelpCommandHandler(), "?");
		shell.addMainHandler(new ScriptCommandHandler(), ".");
		shell.addMainHandler(new VariableInputConverter(), "");
		shell.addMainHandler(new CompleterHandler(), "");
		shell.addMainHandler(new CommandNameCandidatesChooser(), "");
		shell.addMainHandler(new MacroNameCandidatesChooser(), "");
		shell.addMainHandler(new BooleanCandidatesChooser(), "");
		shell.addMainHandler(new EnumCandidatesChooser(), "");
		shell.addMainHandler(new LocaleCandidatesChooser(), "");
		shell.addMainHandler(new VariableCandidatesChooser(), "");
	}

	/**
	 * Build the shell with the settings which you have set before.
	 *
	 * @return A ready-to-use {@link Shell}.
	 */
	public Shell build(){
		try{
			checkPrecondition();
			configure();

			Shell shell = model.getParent() == null ? buildShell() : buildSubShell();

			return shell;
		}catch(IOException e){
			throw new RuntimeException("Could not build a shell!", e);
		}
	}
}
