package de.raysha.lib.jsimpleshell.builder;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.completer.CandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.filter.CandidateFilter;
import de.raysha.lib.jsimpleshell.completer.filter.DefaultPrefixCandidateFilter;
import de.raysha.lib.jsimpleshell.handler.*;
import de.raysha.lib.jsimpleshell.model.CommandDefinition;
import de.raysha.lib.jsimpleshell.script.Environment;
import de.raysha.lib.jsimpleshell.script.cmd.process.ProcessCommandHandler;
import de.raysha.lib.jsimpleshell.script.cmd.process.ProcessResultOutputConverter;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * This is a part of the {@link ShellBuilder}. It is responsible for configuring
 * all stuff that has to do with the behavior of the shell.
 *
 * @author rainu
 */
public class Behavior implements Builder {
	private final BuilderModel model;
	private final Builder parentBuilder;

	Behavior(BuilderModel model, Builder parentBuilder) {
		this.model = model;
		this.parentBuilder = parentBuilder;
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
	 * <li>Implements the {@link CommandLoopObserver} interface to observe the proceeded command lines</li>
	 * <li>Implements the {@link CandidateFilter} interface to get the possibility to filter the chosen candidates for auto completion.</li>
	 * </ul>
	 *
	 * @param handler The command handler.
	 * @return This {@link Behavior}
	 */
	public Behavior addHandler(Object...handler) {
		return addHandler(Arrays.asList(handler));
	}

	/**
	 * @see Behavior#addHandler(Object...)
	 * @param handler The command handler.
	 * @return This {@link Behavior}
	 */
	public Behavior addHandler(Collection<Object> handler) {
		if(handler != null) for(Object h : handler){
			model.getHandlers().add(h);
		}

		return this;
	}

	/**
	 * This method is very similar to addHandler, except ShellBuilder
	 * will pass all handlers registered with this method to all this shell's subshells.
	 *
	 * @see Behavior#addHandler(java.lang.Object)
	 *
	 * @param handler Object which should be registered as handler.
	 * @return This {@link Behavior}
	 */
	public Behavior addAuxHandler(Object...handler){
		return addAuxHandler(Arrays.asList(handler));
	}

	/**
	 * @see Behavior#addAuxHandler(Object...)
	 * @param handler Object which should be registered as handler.
	 * @return This {@link Behavior}
	 */
	public Behavior addAuxHandler(Collection<Object> handler){
		if(handler != null) for(Object h : handler){
			model.getAuxHandlers().put("", h);
		}

		return this;
	}

	/**
	 * Add Main-{@link CommandDefinition}s. For example you can use functions
	 * from the {@link CommandBuilder}.
	 *
	 * @see CommandBuilder
	 * @param commandDefinitions The {@link CommandDefinition}s to be add.
	 * @return This {@link Behavior}
	 */
	public Behavior addMainCommand(Collection<CommandDefinition> commandDefinitions){
		model.getMainCommands().addAll(commandDefinitions);

		return this;
	}

	/**
	 * Add Main-{@link CommandDefinition}s. For example you can use functions
	 * from the {@link CommandBuilder}.
	 *
	 * @see CommandBuilder
	 * @param commandDefinitions The {@link CommandDefinition}s to be add.
	 * @return This {@link Behavior}
	 */
	public Behavior addMainCommand(CommandDefinition... commandDefinition){
		return addMainCommand(Arrays.asList(commandDefinition));
	}

	/**
	 * Add Aux-{@link CommandDefinition}s. For example you can use functions
	 * from the {@link CommandBuilder}.
	 *
	 * @see CommandBuilder
	 * @param commandDefinitions The {@link CommandDefinition}s to be add.
	 * @return This {@link Behavior}
	 */
	public Behavior addAuxCommand(Collection<CommandDefinition> commandDefinitions){
		model.getAuxCommands().addAll(commandDefinitions);

		return this;
	}

	/**
	 * Add Aux-{@link CommandDefinition}s. For example you can use functions
	 * from the {@link CommandBuilder}.
	 *
	 * @see CommandBuilder
	 * @param commandDefinitions The {@link CommandDefinition}s to be add.
	 * @return This {@link Behavior}
	 */
	public Behavior addAuxCommand(CommandDefinition... commandDefinition){
		return addAuxCommand(Arrays.asList(commandDefinition));
	}

	/**
	 * Should the shell handle user interrupts (CRTL-C)?
	 *
	 * @param handleUserInterrupt
	 *            False will cause that the JVM will handle SIGINT as normal, which usually
	 *            causes it to exit. True will cause that the JVM will not handle the signal.
	 * @return This {@link Behavior}
	 */
	public Behavior setHandleUserInterrupt(boolean handleUserInterrupt) {
		model.setHandleUserInterrupt(handleUserInterrupt);

		return this;
	}

	/**
	 * Disable the file name completion mechanism!
	 *
	 * @return This {@link Behavior}
	 */
	public Behavior disableFileNameCompleter(){
		model.setFileNameCompleterEnabled(false);

		return this;
	}

	/**
	 * Enable the file name completion mechanism!
	 *
	 * @return This {@link Behavior}
	 */
	public Behavior enableFileNameCompleter(){
		model.setFileNameCompleterEnabled(true);

		return this;
	}

	/**
	 * Enable or disable the file name completion mechanism.
	 *
	 * @param enable True if the mechanism should be enabled. Otherwise false.
	 * @return This {@link Behavior}
	 */
	public Behavior setFileNameCompleter(boolean enable){
		if(enable){
			enableFileNameCompleter();
		}else{
			disableFileNameCompleter();
		}

		return this;
	}

	/**
	 * Enable the exit command. By default it is enabled!
	 *
	 * @return This {@link Behavior}
	 */
	public Behavior enableExitCommand(){
		model.setDisableExit(false);

		return this;
	}

	/**
	 * @see Shell#disableExitCommand()
	 */
	public Behavior disableExitCommand(){
		return disableExitCommand(null);
	}

	/**
	 * @see Shell#disableExitCommand(String)
	 */
	public Behavior disableExitCommand(String exitCommand){
		model.setDisableExit(true);
		model.setExitCommand(exitCommand);

		return this;
	}

	/**
	 * Enable or disable the exit command.
	 *
	 * @param enable True if the exit command should be enabled. Otherwise false.
	 * @return This {@link Behavior}
	 */
	public Behavior setExitCommand(boolean enable){
		if(enable){
			enableExitCommand();
		}else{
			disableExitCommand();
		}

		return this;
	}

	/**
	 * Set the location of the history. By default the history will be not persisted!
	 *
	 * @param historyFile The location of the history. Null means that the history will not persisted.
	 * @return This {@link Behavior}
	 */
	public Behavior setHistoryFile(File historyFile){
		model.setHistory(historyFile);

		return this;
	}

	/**
	 * Sets the directory where the macros are stored. This must be an <b>existing directory</b>!
	 *
	 * @param macroHome The new macro home directory.
	 * @return This {@link Behavior}
	 */
	public Behavior setMacroHome(File macroHome){
		model.setMacroHome(macroHome);

		return this;
	}

	/**
	 * Enable the special commands that can start an external process. For security reasons,
	 * this commands are disabled by default.
	 *
	 * @see ProcessCommandHandler
	 * @return This {@link Behavior}
	 */
	public Behavior enableProcessStarterCommands(){
		disableProcessStarterCommands(); //remove duplicates

		model.getAuxHandlers().put(".", new ProcessCommandHandler());
		model.getAuxHandlers().put("", new ProcessResultOutputConverter());

		return this;
	}

	/**
	 * Disable the special commands that can start an external process. For security reasons,
	 * this commands are disabled by default.
	 *
	 * @see ProcessCommandHandler
	 * @return This {@link Behavior}
	 */
	public Behavior disableProcessStarterCommands(){
		Iterator<Object> iter = model.getAuxHandlers().get(".").iterator();
		while(iter.hasNext()){
			if(iter.next() instanceof ProcessCommandHandler){
				iter.remove();
			}
		}

		iter = model.getAuxHandlers().get("").iterator();
		while(iter.hasNext()){
			if(iter.next() instanceof ProcessResultOutputConverter){
				iter.remove();
			}
		}

		return this;
	}

	/**
	 * Disable or enable the special commands that can start an external process.
	 * For security reasons, this commands are disabled by default.
	 *
	 * @param enable True if the special commands should be enabled. Otherwise false.
	 * @see ProcessCommandHandler
	 * @return This {@link Behavior}
	 */
	public Behavior setProcessStarterCommands(boolean enable){
		if(enable){
			enableProcessStarterCommands();
		}else{
			disableProcessStarterCommands();
		}

		return this;
	}

	/**
	 * Disable the auto completion of special commands. Special commands are the commands
	 * which starts with a prefix (e.g "?").
	 *
	 * @return This {@link Behavior}
	 */
	public Behavior disableAutocompleOfSpecialCommands(){
		enableAutocompleOfSpecialCommands();

		addHandler(new DefaultPrefixCandidateFilter("?", ".", "!"));

		return this;
	}

	/**
	 * Enable the auto completion of special commands. Special commands are the commands
	 * which starts with a prefix (e.g "?").
	 *
	 * @return This {@link Behavior}
	 */
	public Behavior enableAutocompleOfSpecialCommands(){
		Iterator<Object> iter = model.getHandlers().iterator();
		while(iter.hasNext()){
			if(iter.next() instanceof DefaultPrefixCandidateFilter){
				iter.remove();
			}
		}

		return this;
	}

	/**
	 * Disable or enable the auto completion of special commands. Special commands are the commands
	 * which starts with a prefix (e.g "?").
	 *
	 * @param enable True if the auto completion should be enabled. Otherwise false.
	 * @return This {@link Behavior}
	 */
	public Behavior setAutocompleOfSpecialCommands(boolean enable){
		if(enable){
			enableAutocompleOfSpecialCommands();
		}else{
			disableAutocompleOfSpecialCommands();
		}

		return this;
	}

	@Override
	public Behavior behavior() {
		return this;
	}

	@Override
	public IO io() {
		return new IO(model, this);
	}

	@Override
	public Look look() {
		return new Look(model, this);
	}

	@Override
	public ShellBuilder root() {
		if(parentBuilder instanceof ShellBuilder)
			return (ShellBuilder) parentBuilder;

		return parentBuilder.root();
	}

	@Override
	public Shell build() {
		return parentBuilder.build();
	}

}
