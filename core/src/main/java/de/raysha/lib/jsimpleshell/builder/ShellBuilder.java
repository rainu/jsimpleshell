package de.raysha.lib.jsimpleshell.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jline.console.ConsoleReader;
import jline.console.history.FileHistory;
import de.raysha.lib.jsimpleshell.CommandPipeline;
import de.raysha.lib.jsimpleshell.CommandTable;
import de.raysha.lib.jsimpleshell.DashJoinedNamer;
import de.raysha.lib.jsimpleshell.HelpCommandHandler;
import de.raysha.lib.jsimpleshell.HistoryFlusher;
import de.raysha.lib.jsimpleshell.PromptElement;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.ShellSettings;
import de.raysha.lib.jsimpleshell.completer.BooleanCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.CommandNameCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.EnumCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.FileCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.LocaleCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.MacroNameCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.VariableCandidatesChooser;
import de.raysha.lib.jsimpleshell.io.TerminalIO;
import de.raysha.lib.jsimpleshell.script.Environment;
import de.raysha.lib.jsimpleshell.script.Variable;
import de.raysha.lib.jsimpleshell.script.VariableInputConverter;
import de.raysha.lib.jsimpleshell.script.cmd.EchoCommandHandler;
import de.raysha.lib.jsimpleshell.script.cmd.EnvironmentCommandHandler;
import de.raysha.lib.jsimpleshell.script.cmd.LoopCommandHandler;
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

	private ShellBuilder(){
		setDefaultSettings();
	}

	private void setDefaultSettings() {
		this.behavior().enableExitCommand();
		this.behavior().enableFileNameCompleter();
		this.behavior().disableProcessStarterCommands();
		this.behavior().disableAutocompleOfSpecialCommands();

		this.look().enableColor();
	}

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
	 * Enter the part that is responsible for configuring the look of the shell.
	 *
	 * @return The {@link Look}.
	 */
	public Look look(){
		return new Look(model, this);
	}

	/**
	 * Enter the part that is responsible for configuring the behavior of the shell.
	 *
	 * @return The {@link Behavior}.
	 */
	public Behavior behavior(){
		return new Behavior(model, this);
	}

	/**
	 * Enter the part that is responsible for configuring the input/output of the shell.
	 *
	 * @return The {@link IO}.
	 */
	public IO io(){
		return new IO(model, this);
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
				new CommandTable(new DashJoinedNamer(true)), path, new CommandPipeline(), buildInitialEnvironment());

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
				new CommandTable(parent.getCommandTable().getNamer()), newPath, parent.getPipeline(), copyEnvironment(parent));

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
		shell.addMainHandler(new EnvironmentCommandHandler(), ".");
		shell.addMainHandler(new LoopCommandHandler(), ".");
		shell.addMainHandler(new EchoCommandHandler(), ".");
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
