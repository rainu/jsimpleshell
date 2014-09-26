package de.raysha.lib.jsimpleshell.handler;

import de.raysha.lib.jsimpleshell.ShellCommand;
import de.raysha.lib.jsimpleshell.exception.ExitException;

/**
 * Classes that want to inform about command executing should implement this interface.
 *
 * @author rainu
 */
public interface CommandHookDependent {

	public static class ExecutionResult {
		private final Object result;
		private final Throwable thrown;
		private final Long executionTime;

		public ExecutionResult(Object result, Long executionTime) {
			this.result = result;
			this.thrown = null;
			this.executionTime = executionTime;
		}

		public ExecutionResult(Throwable thrown, Long executionTime) {
			this.result = null;
			this.thrown = thrown;
			this.executionTime = executionTime;
		}

		/**
		 * Get the result that the command returned.
		 *
		 * @return The return value of the command.
		 */
		public Object getResult() {
			return result;
		}

		/**
		 * Get the {@link Throwable} that the command was thrown.
		 *
		 * @return The {@link Throwable} or null if no {@link Throwable} was thrown.
		 */
		public Throwable getThrown() {
			return thrown;
		}

		/**
		 * Get the duration of the command execution.
		 *
		 * @return The duration of command execution.
		 */
		public Long getExecutionTime() {
			return executionTime;
		}

		/**
		 * Was the execution successful? The execution is also considered
		 * as successful if an {@link ExitException} was thrown!
		 *
		 * @return True if the execution was successful. Otherwise false.
		 */
		public boolean wasExecutionSuccessful(){
			return getThrown() == null || getThrown() instanceof ExitException;
		}
	}

	/**
	 * This method will be called before a command will be executed.
	 *
	 * @param command Which command will be executed.
	 */
	public void cliBeforeCommand(ShellCommand command);

	/**
	 * This method will be called after a command was executed.
	 *
	 * @param command Which command was executed.
	 * @param result The execution result.
	 */
	public void cliAfterCommand(ShellCommand command, ExecutionResult result);
}
