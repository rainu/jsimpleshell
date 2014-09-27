package de.raysha.lib.jsimpleshell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.WeakHashMap;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;
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
	private final static WeakHashMap<ConsoleReader, AggregateCompleter> aggregateCompleter = new WeakHashMap<ConsoleReader, AggregateCompleter>();
	private final static WeakHashMap<Shell, Collection<Completer>> shellCompleterRelation = new WeakHashMap<Shell, Collection<Completer>>();
	private final static WeakHashMap<Shell, Collection<Completer>> shellPrevCompleterRelation = new WeakHashMap<Shell, Collection<Completer>>();

	@Override
	public void cliEnterLoop(Shell shell) {
		if(shell.getSettings().input instanceof TerminalIO){
			ConsoleReader console = ((TerminalIO)shell.getSettings().input).getConsole();

			removePreviousCompleter(shell, console);
			addCompleter(shell, console);
		}
	}

	@Override
	public void cliLeaveLoop(Shell shell) {
		if(shell.getSettings().input instanceof TerminalIO){
			ConsoleReader console = ((TerminalIO)shell.getSettings().input).getConsole();

			removeCompleter(shell, console);
			restorePreviousCompleter(shell, console);
		}
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
		completer.add(new CommandNameCompleter(shell.accessManager, shell.getCommandTable().getCommandTable()));
		completer.add(new ParameterCompleter(shell.getCommandTable(), shell.candidatesChooser));

		for(Completer c : completer){
			completerContainer.getCompleters().add(c);
		}

		shellCompleterRelation.put(shell, completer);
	}
}