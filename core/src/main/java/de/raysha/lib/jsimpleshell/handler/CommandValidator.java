package de.raysha.lib.jsimpleshell.handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.raysha.lib.jsimpleshell.ShellCommand;
import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;

/**
 * Each class which wants to validate a command (and his parameters) must implements this interface.
 *
 * @author rainu
 */
public interface CommandValidator {

	/**
	 * Validate the command (included in the context).
	 *
	 * @param ctx The command context.
	 * @return <b>Always</b> a {@link ValidationResult} that contains the validation failures (if there was any).
	 */
	public ValidationResult validate(Context ctx);

	/**
	 * This class contains relevant data about a command execution.
	 *
	 * @author rainu
	 */
	public static class Context {
		private final ShellCommand command;
		private final Object[] commandParameters;

		public Context(ShellCommand command) {
			this(command, null);
		}

		public Context(ShellCommand command, Object[] commandParameters) {
			this.command = command;
			this.commandParameters = commandParameters;
		}

		/**
		 * Get the current command.
		 *
		 * @return The current command.
		 */
		public ShellCommand getCommand() {
			return command;
		}
		/**
		 * Get the used command parameters. An <b>empty array</b> means the user entered no parameters.
		 * But <b>null</b> means the user dosen't have execute the command and the shell wants to know if the
		 * user has general access to this command (Maybe for auto completion reason). In this case the check-
		 * duration <b>should be as minimal as possible</b>! Because each command in the current command-
		 * table will be checked from the access manager.
		 *
		 * @return The used command parameters.
		 */
		public Object[] getCommandParameters() {
			return commandParameters;
		}
	}

	/**
	 * This class represents the command validation result.
	 *
	 * @author rainu
	 */
	public static class ValidationResult {
		private final ShellCommand command;
		private final Object[] commandParameters;
		private final Map<ShellCommandParamSpec, String> failMessages = new HashMap<ShellCommandParamSpec, String>();

		public ValidationResult(ShellCommand command, Object[] commandParameters) {
			this.command = command;
			this.commandParameters = commandParameters;
		}

		/**
		 * Creates a validation result for the given {@link Context}. After that you can
		 * put failures in there.
		 *
		 * @param ctx The validation context.
		 * @return
		 */
		public static ValidationResult forContext(Context ctx){
			return new ValidationResult(ctx.getCommand(), ctx.getCommandParameters());
		}

		/**
		 * Adds an failure for that validation.
		 *
		 * @param param The parameter which was invalid.
		 * @param message The message for the user.
		 * @return This {@link ValidationResult}
		 */
		public ValidationResult addFailure(ShellCommandParamSpec param, String message){
			failMessages.put(param, message);

			return this;
		}

		/**
		 * Return the failures for this validation.
		 *
		 * @return A map which contains the failures. Or an empty map if there is no failure.
		 */
		public Map<ShellCommandParamSpec, String> getFailures() {
			return Collections.unmodifiableMap(failMessages);
		}

		/**
		 * Check if this result contains any failures.
		 *
		 * @return True if there is any failures. Otherwise false.
		 */
		public boolean hasFailures() {
			return !failMessages.isEmpty();
		}

		@Override
		public String toString() {
			return failMessages.toString();
		}
	}
}
