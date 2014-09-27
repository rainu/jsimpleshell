package de.raysha.lib.jsimpleshell.handler;

import de.raysha.lib.jsimpleshell.ShellCommand;

/**
 * Each class which wants to check the permission for each command must implements this interface.
 *
 * @author rainu
 */
public interface CommandAccessManager {

	/**
	 * Checks whether the given command may be executed.
	 *
	 * @param context The command context.
	 * @return The {@link AccessDecision}. If the decision is negative, the command is not executed.
	 */
	public AccessDecision checkCommandPermission(Context context);

	/**
	 * This class includes relevant data about a command execution.
	 *
	 * @author rainu
	 */
	public static class Context {
		private final ShellCommand command;
		private final Object[] commandParameters;

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
		 * user has general access to this command (Maybe for auto completion reason).
		 *
		 * @return The used command parameters.
		 */
		public Object[] getCommandParameters() {
			return commandParameters;
		}
	}

	/**
	 * This class represents an access decision.
	 *
	 * @author rainu
	 */
	public static class AccessDecision {
		public static enum Decision {
			ALLOWED, DENIED;
		}

		private final Decision decision;
		private final String reason;

		public AccessDecision(Decision decision) {
			this.decision = decision;
			this.reason = null;
		}

		public AccessDecision(Decision decision, String reason) {
			this.decision = decision;
			this.reason = reason;
		}

		/**
		 * Get the decision. If they is negative, the command should not be executed.
		 *
		 * @return The final decision.
		 */
		public Decision getDecision() {
			return decision;
		}
		/**
		 * Get the (optional) reason message.
		 *
		 * @return The reason or null if no reason is given.
		 */
		public String getReason() {
			return reason;
		}
	}
}
