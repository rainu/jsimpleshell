package de.raysha.lib.jsimpleshell.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.completer.CommandNameCompleter;
import de.raysha.lib.jsimpleshell.completer.ParameterCompleter;
import de.raysha.lib.jsimpleshell.handler.ShellManageable;
import de.raysha.lib.jsimpleshell.io.TerminalIO;

/**
 * This handler is responsible for de-/registraion of {@link Completer} to a {@link ConsoleReader}.
 * With other words: this class is responsible for enabling auto-completion.
 *
 * @author rainu
 */
class CompleterHandler implements ShellManageable {
	private final static Map<Shell, Collection<Completer>> shellCompleterRelation = new HashMap<Shell, Collection<Completer>>();
	private final static Map<Shell, Collection<Completer>> shellPrevCompleterRelation = new HashMap<Shell, Collection<Completer>>();

	@Override
	public void cliEnterLoop(Shell shell) {
		if(shell.getSettings().getInput() instanceof TerminalIO){
			ConsoleReader console = ((TerminalIO)shell.getSettings().getInput()).getConsole();

			removePreviousCompleter(shell, console);
			addCompleter(shell, console);
		}
	}

	@Override
	public void cliLeaveLoop(Shell shell) {
		if(shell.getSettings().getInput() instanceof TerminalIO){
			ConsoleReader console = ((TerminalIO)shell.getSettings().getInput()).getConsole();

			removeCompleter(shell, console);
			restorePreviousCompleter(shell, console);
		}

		shellCompleterRelation.remove(shell);
		shellPrevCompleterRelation.remove(shell);
	}

	private void restorePreviousCompleter(Shell shell, ConsoleReader console) {
		if(!shellPrevCompleterRelation.containsKey(shell)){
			return; //this is the root shell
		}

		AggregateCompleter completerContainer = getCompleter(console);
		Collection<Completer> prevCompleters = shellPrevCompleterRelation.get(shell);

		if(completerContainer != null){
			for(Completer c : prevCompleters){
				completerContainer.getCompleters().add(c);
			}
		}
	}

	private void removePreviousCompleter(Shell shell, ConsoleReader console) {
		AggregateCompleter completerContainer = getCompleter(console);

		shellPrevCompleterRelation.put(shell, new ArrayList<Completer>(completerContainer.getCompleters()));
		completerContainer.getCompleters().clear();
	}

	private void removeCompleter(Shell shell, ConsoleReader console) {
		AggregateCompleter completerContainer = getCompleter(console);

		for(Completer c : shellCompleterRelation.get(shell)){
			completerContainer.getCompleters().remove(c);
		}
	}

	private void addCompleter(Shell shell, ConsoleReader console) {
		AggregateCompleter completerContainer = getCompleter(console);

		List<Completer> completer = new ArrayList<Completer>();
		completer.add(buildCommandNameCompleter(shell));
		completer.add(buildParameterCompleter(shell));

		for(Completer c : completer){
			completerContainer.getCompleters().add(c);
		}

		shellCompleterRelation.put(shell, completer);
	}

	private ParameterCompleter buildParameterCompleter(Shell shell) {
		ParameterCompleter completer = new ParameterCompleter(shell.getCommandTable(), shell.getCandidatesChooser());
		completer.setFilter(shell.getCandidatesFilter());

		return completer;
	}

	private CommandNameCompleter buildCommandNameCompleter(Shell shell) {
		CommandNameCompleter completer = new CommandNameCompleter(shell.getAccessManager(), shell.getCommandTable().getCommandTable());
		completer.setFilter(shell.getCandidatesFilter());

		return completer;
	}

	private AggregateCompleter getCompleter(ConsoleReader console){
		if(console.getCompleters() != null) for(Completer completer : console.getCompleters()){
			if(completer instanceof MyAggregateCompleter){
				//there should be only one of them
				return (MyAggregateCompleter)completer;
			}
		}

		MyAggregateCompleter completer = new MyAggregateCompleter();
		console.addCompleter(completer);

		return completer;
	}

	private class MyAggregateCompleter extends AggregateCompleter {

	}
}
