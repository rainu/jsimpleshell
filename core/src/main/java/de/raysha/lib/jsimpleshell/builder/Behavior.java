package de.raysha.lib.jsimpleshell.builder;

import java.io.File;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.completer.CandidatesChooser;
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
import de.raysha.lib.jsimpleshell.script.Environment;

/**
 * This is a part of the {@link ShellBuilder}. It is responsible for configuring
 * all stuff that has to do with the behavior of the shell.
 *
 * @author rainu
 */
public class Behavior {
	private final BuilderModel model;
	private final ShellBuilder shellBuilder;

	Behavior(BuilderModel model, ShellBuilder shellBuilder) {
		this.model = model;
		this.shellBuilder = shellBuilder;
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
	 * @return This {@link Behavior}
	 */
	public Behavior addHandler(Object handler) {
		model.getHandlers().add(handler);

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
	public Behavior addAuxHandler(Object handler){
		model.getAuxHandlers().put("", handler);

		return this;
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
	 * Disable the exit command. Be careful! You must implements
	 * your own exit mechanism (@see ExitException). If you do not,
	 * the user can never exit the shell normally!
	 *
	 * @return This {@link Behavior}
	 */
	public Behavior disableExitCommand(){
		model.setDisableExit(true);

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
	 * Go back to the {@link ShellBuilder}.
	 *
	 * @return The {@link ShellBuilder}
	 */
	public ShellBuilder back(){
		return shellBuilder;
	}
}
