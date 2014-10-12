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
	/*
	 * It is VERY important to use a WeakMap. Otherwise the ConsoleReader and/or the Shell instances can never be garbage collected!
	 */
	private final static Map<ConsoleReader, AggregateCompleter> aggregateCompleter = new HashMap<ConsoleReader, AggregateCompleter>();
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

			//it is very important to remove this relations from my map, otherwise the garbage collector
			//could not delete this instances from space!
			aggregateCompleter.remove(console);
		}

		shellCompleterRelation.remove(shell);
		shellPrevCompleterRelation.remove(shell);
	}

	private void restorePreviousCompleter(Shell shell, ConsoleReader console) {
		if(!shellPrevCompleterRelation.containsKey(shell)){
			return; //this is the root shell
		}

		AggregateCompleter completerContainer = aggregateCompleter.get(console);
		Collection<Completer> prevCompleters = shellPrevCompleterRelation.get(shell);

		for(Completer c : prevCompleters){
			completerContainer.getCompleters().add(c);
		}
	}

	private void removePreviousCompleter(Shell shell, ConsoleReader console) {
		AggregateCompleter completerContainer = aggregateCompleter.get(console);
		if(completerContainer == null){
			return; //this is the root shell
		}

		shellPrevCompleterRelation.put(shell, new ArrayList<Completer>(completerContainer.getCompleters()));
		completerContainer.getCompleters().clear();
	}

	private void removeCompleter(Shell shell, ConsoleReader console) {
		if(aggregateCompleter.containsKey(console)){
			for(Completer c : shellCompleterRelation.get(shell)){
				aggregateCompleter.get(console).getCompleters().remove(c);
			}
		}
	}

	private void addCompleter(Shell shell, ConsoleReader console) {
		if(!aggregateCompleter.containsKey(console)){
			AggregateCompleter completer = new AggregateCompleter();
			aggregateCompleter.put(console, completer);

			console.addCompleter(completer);
		}

		AggregateCompleter completerContainer = aggregateCompleter.get(console);

		List<Completer> completer = new ArrayList<Completer>();
		completer.add(new CommandNameCompleter(shell.getAccessManager(), shell.getCommandTable().getCommandTable()));
		completer.add(new ParameterCompleter(shell.getCommandTable(), shell.getCandidatesChooser()));

		for(Completer c : completer){
			completerContainer.getCompleters().add(c);
		}

		shellCompleterRelation.put(shell, completer);
	}
}
