package de.rainu.lib.jsimpleshell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import jline.console.ConsoleReader;
import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;
import de.rainu.lib.jsimpleshell.annotation.Command;
import de.rainu.lib.jsimpleshell.io.InputConverter;
import de.rainu.lib.jsimpleshell.io.InputDependent;
import de.rainu.lib.jsimpleshell.io.OutputConverter;
import de.rainu.lib.jsimpleshell.io.OutputDependent;
import de.rainu.lib.jsimpleshell.io.TerminalIO;
import de.rainu.lib.jsimpleshell.util.ArrayHashMultiMap;
import de.rainu.lib.jsimpleshell.util.EmptyMultiMap;
import de.rainu.lib.jsimpleshell.util.MultiMap;

/**
 * This is a class that can be used to build a {@link Shell}.
 * 
 * @author rainu
 */
public class ShellBuilder {
	private String prompt;
	private String appName = "JSimpleShell";
	private MultiMap<String, Object> auxHandlers = new EmptyMultiMap<String, Object>();
	private Collection<Object> handlers = new LinkedList<Object>();
	private Shell parent;
	private ConsoleReader console;
	private OutputStream error = System.err;
	private boolean useForeignConsole = false;
	private boolean fileNameCompleterEnabled = true;
	private boolean commandCompleterEnabled = true;
	
	private ShellBuilder(){ }
	
	/**
	 * Create a new {@link ShellBuilder} instance with which one you can build a {@link Shell}.
	 * 
	 * @param subPrompt The promt for this shell.
	 * @return A new instance of {@link ShellBuilder}
	 * @throws NullPointerException If the prompt is null.
	 */
	public static ShellBuilder shell(String prompt){
		if(prompt == null){
			throw new NullPointerException("The prompt must not be null!");
		}
		
		ShellBuilder builder = new ShellBuilder();
		
		builder.prompt = prompt;
		
		return builder;
	}
	
	/**
	 * Create a new {@link ShellBuilder} instance with which one you can build a Sub{@link Shell}.
	 * 
	 * @param subPrompt The sub-promt for this sub shell.
	 * @param parent The parent {@link Shell} instance.
	 * @return A new instance of {@link ShellBuilder}
	 * @throws NullPointerException If the prompt is null.
	 */
	public static ShellBuilder subshell(String subPrompt, Shell parent){
		if(subPrompt == null){
			throw new NullPointerException("The prompt must not be null!");
		}
		
		ShellBuilder builder = new ShellBuilder();
		
		builder.prompt = subPrompt;
		builder.parent = parent;
		
		return builder;
	}
	
	/**
	 * Set the app name.
	 * 
	 * @param appName The app name.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setAppName(String appName) {
		this.appName = appName;
		return this;
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
	 * </ul>
	 *
	 * @param handler A command handler.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder addHandler(Object handler) {
		handlers.add(handler);
		return this;
	}

	/**
	 * Set the {@link ConsoleReader} which you want to use.
	 * 
	 * @param console The {@link ConsoleReader} instance.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setConsole(ConsoleReader console) {
		this.console = console;
		this.useForeignConsole = true;
		
		return this;
	}
	
	/**
	 * Set Error {@link OutputStream} which will be used for printing error messages.
	 * 
	 * @param error The {@link OutputStream} instance.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setError(OutputStream error) {
		this.error = error;
		return this;
	}
	
	/**
	 * Create and use a new {@link ConsoleReader} with the specified
	 * Input- and Output- Stream.
	 * 
	 * @param in The {@link InputStream} that should be used.
	 * @param out The {@link OutputStream} that should be used.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setConsole(InputStream in, OutputStream out) throws IOException {
		this.console = new ConsoleReader(in, out);
		this.useForeignConsole = false;
		
		return this;
	}
	
	/**
	 * Disable the file name completion mechanism! 
	 * 
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder disableFileNameCompleter(){
		this.fileNameCompleterEnabled = false;
		return this;
	}
	
	/**
	 * Enable the file name completion mechanism! 
	 * 
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder enableFileNameCompleter(){
		this.fileNameCompleterEnabled = true;
		return this;
	}
	
	/**
	 * Disable the command name completion mechanism! 
	 * 
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder disableCommandCompleter(){
		this.commandCompleterEnabled = false;
		return this;
	}
	
	/**
	 * Enable the command name completion mechanism! 
	 * 
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder enableCommandCompleter(){
		this.commandCompleterEnabled = true;
		return this;
	}
	
	private void checkPrecondition() throws IOException {
		if(console == null){
			if(parent == null){
				console = new ConsoleReader();
			}else if(parent.getSettings().input instanceof TerminalIO){
				console = ((TerminalIO)parent.getSettings().input).getConsole();
			}else{
				throw new IllegalStateException("The parent shell seams to be not built with ShellBuilder!");
			}
		}
	}
	
	private void configure() {
		if(!useForeignConsole){
			if(fileNameCompleterEnabled){
				console.addCompleter(new FileArgumentCompleter());
			}
		}
	}
	
	private void configure(Shell shell) {
		if(commandCompleterEnabled){
			//remove old
			removeOldCommandNameCompleter();
			
			Collection<String> commandsNames = new HashSet<String>();
			for(ShellCommand cmd : shell.getCommandTable().getCommandTable()){
				commandsNames.add(cmd.getPrefix() + cmd.getName());
			}
			
			console.addCompleter(new StringsCompleter(commandsNames));
		}
	}
	
	private void removeOldCommandNameCompleter() {
		Completer toRemove = null;
		
		if(console.getCompleters() != null) for(Completer c : console.getCompleters()){
			if(c instanceof StringsCompleter){
				toRemove = c;
				break;
			}
		}
		
		console.removeCompleter(toRemove);
	}

	private Shell buildShell() {
		TerminalIO io = new TerminalIO(console, error);

        List<String> path = new ArrayList<String>(1);
        path.add(prompt);

        MultiMap<String, Object> modifAuxHandlers = new ArrayHashMultiMap<String, Object>(auxHandlers);
        modifAuxHandlers.put("!", io);

        Shell theShell = new Shell(new Shell.Settings(io, io, modifAuxHandlers, false),
                new CommandTable(new DashJoinedNamer(true)), path);
        theShell.setAppName(appName);

        theShell.addMainHandler(theShell, "!");
        theShell.addMainHandler(new HelpCommandHandler(), "?");
        for (Object h : handlers) {
            theShell.addMainHandler(h, "");
        }

        return theShell;
	}
	
	private Shell buildSubShell() {
		List<String> newPath = new ArrayList<String>(parent.getPath());
        newPath.add(prompt);

        Shell subshell = new Shell(parent.getSettings().createWithAddedAuxHandlers(auxHandlers),
                new CommandTable(parent.getCommandTable().getNamer()), newPath);

        subshell.setAppName(appName);
        subshell.addMainHandler(subshell, "!");
        subshell.addMainHandler(new HelpCommandHandler(), "?");

        for (Object h : handlers) {
        	subshell.addMainHandler(h, "");
        }
        return subshell;
	}
	
	/**
	 * Build the shell with the settings which you have set before.
	 * 
	 * @return A ready-to-use {@link Shell}.
	 */
	public Shell build(){
		try{
			checkPrecondition();
			configure();
			
			Shell shell = parent == null ? buildShell() : buildSubShell();
			configure(shell);
			
			return shell;
		}catch(IOException e){
			throw new RuntimeException("Could not build a shell!", e);
		}
	}
}