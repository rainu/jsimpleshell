package de.raysha.lib.jsimpleshell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.WeakHashMap;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;
import de.raysha.lib.jsimpleshell.handler.ShellManageable;
import de.raysha.lib.jsimpleshell.io.TerminalIO;

/**
 * This handler is responsible for de-/registraion of {@link Completer} to a {@link ConsoleReader}. 
 * With other words: this class is responsible for enabling command auto-completion.
 * 
 * @author rainu
 */
class CommandCompleterHandler implements ShellManageable {
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

			removePreviousCommandCompleter(shell, console);
			addCommandNameCompleter(shell, console);
		}
	}

	@Override
	public void cliLeaveLoop(Shell shell) {
		if(shell.getSettings().input instanceof TerminalIO){
			ConsoleReader console = ((TerminalIO)shell.getSettings().input).getConsole();
			
			removeCommandNameCompleter(shell, console);
			restorePreviousCommandCompleter(shell, console);
		}
	}
	
	private void restorePreviousCommandCompleter(Shell shell, ConsoleReader console) {
		if(!shellPrevCompleterRelation.containsKey(shell)){
			return; //this is the root shell
		}
		
		AggregateCompleter completerContainer = aggregateCompleter.get(console);
		Collection<Completer> prevCompleters = shellPrevCompleterRelation.get(shell);
		
		for(Completer c : prevCompleters){
			completerContainer.getCompleters().add(c);
		}
	}

	private void removePreviousCommandCompleter(Shell shell, ConsoleReader console) {
		AggregateCompleter completerContainer = aggregateCompleter.get(console);
		if(completerContainer == null){
			return; //this is the root shell
		}
		
		shellPrevCompleterRelation.put(shell, new ArrayList<Completer>(completerContainer.getCompleters()));
		completerContainer.getCompleters().clear();
	}
	
	private void removeCommandNameCompleter(Shell shell, ConsoleReader console) {
		if(aggregateCompleter.containsKey(console)){
			for(Completer c : shellCompleterRelation.get(shell)){
				aggregateCompleter.get(console).getCompleters().remove(c);
			}
		}
	}
	
	private void addCommandNameCompleter(Shell shell, ConsoleReader console) {
		if(!aggregateCompleter.containsKey(console)){
			AggregateCompleter completer = new AggregateCompleter();
			aggregateCompleter.put(console, completer);
			
			console.addCompleter(completer);
		}
		
		AggregateCompleter completerContainer = aggregateCompleter.get(console);
		
		Collection<String> commandNames = new HashSet<String>();
		for(ShellCommand cmd : shell.getCommandTable().getCommandTable()){
			commandNames.add(cmd.getPrefix() + cmd.getName());
		}
		
		List<Completer> completer = new ArrayList<Completer>();
		completer.add(new StringsCompleter(commandNames));
		completer.add(new HelpCompleter(commandNames));
		
		for(Completer c : completer){
			completerContainer.getCompleters().add(c);
		}

		shellCompleterRelation.put(shell, completer);
	}
}