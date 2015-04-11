package de.raysha.lib.jsimpleshell.example;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.ShellCommand;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision;
import de.raysha.lib.jsimpleshell.handler.CommandHookDependent;
import de.raysha.lib.jsimpleshell.handler.CommandLoopObserver;
import de.raysha.lib.jsimpleshell.handler.ShellManageable;

/**
 * This class contains all available hooks. The only thing what is to do is implements
 * the right interfaces.
 *
 * @author rainu
 */
public class Hooks implements CommandLoopObserver, CommandHookDependent, ShellManageable {

	@Override
	public void cliBeforeCommandLine(String line) {
		System.out.println("Before line " + line);
	}

	@Override
	public void cliAfterCommandLine(String line) {
		System.out.println("After line " + line);
	}

	@Override
	public void cliBeforeCommand(ShellCommand command, Object[] parameter) {
		System.out.println("Before command " + command.getName());
	}

	@Override
	public void cliAfterCommand(ShellCommand command, Object[] parameter, ExecutionResult result) {
		System.out.println("After command " + command.getName());
	}

	@Override
	public void cliDeniedCommand(ShellCommand command, Object[] parameter, AccessDecision decision) {
		System.out.println("Command " + command.getName() + " denied.");
	}

	@Override
	public void cliEnterLoop(Shell shell) {
		System.out.println("The shell enters the loop.");
	}

	@Override
	public void cliLeaveLoop(Shell shell) {
		System.out.println("The shell leaves the loop.");
	}

}
