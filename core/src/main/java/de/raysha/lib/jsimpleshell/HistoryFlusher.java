package de.raysha.lib.jsimpleshell;

import java.io.IOException;

import jline.console.ConsoleReader;
import jline.console.history.FileHistory;
import de.raysha.lib.jsimpleshell.handler.ShellManageable;
import de.raysha.lib.jsimpleshell.io.TerminalIO;

/**
 * This handler is responsible for flushing the history (if a {@link ConsoleReader} and {@link FileHistory} will be used)
 *
 * @author rainu
 */
public class HistoryFlusher implements ShellManageable {

	@Override
	public void cliEnterLoop(Shell shell) {
	}

	@Override
	public void cliLeaveLoop(Shell shell) {
		if(shell.getSettings().getInput() instanceof TerminalIO){
			final ConsoleReader console = ((TerminalIO)shell.getSettings().getInput()).getConsole();
			if(console.getHistory() instanceof FileHistory){
				try {
					((FileHistory)console.getHistory()).flush();
				} catch (IOException e) { }
			}
		}
	}

}
