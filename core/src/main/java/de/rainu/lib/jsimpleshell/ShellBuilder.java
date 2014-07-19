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
import jline.console.completer.StringsCompleter;
import de.rainu.lib.jsimpleshell.util.ArrayHashMultiMap;
import de.rainu.lib.jsimpleshell.util.MultiMap;

/**
 * This is a class that can be used to build a {@link Shell}.
 * 
 * @author rainu
 */
public class ShellBuilder {
	private String prompt = "$> ";
	private String appName = "JSimpleShell";
	private Collection<Object> handlers = new LinkedList<Object>();
	private ConsoleReader console;
	private OutputStream error = System.err;
	private boolean useForeignConsole = false;
	private boolean fileNameCompleterEnabled = true;
	private boolean commandCompleterEnabled = true;
	
	/**
	 * Set the Propt to be displayed.
	 * 
	 * @param prompt Prompt to be displayed
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setPrompt(String prompt) {
		this.prompt = prompt;
		return this;
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
	 * Add a command handler. A command handler should contains 
	 * methods that are annotated with {@link Command}.
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
			console = new ConsoleReader();
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
			Collection<String> commandsNames = new HashSet<String>();
			for(ShellCommand cmd : shell.getCommandTable().getCommandTable()){
				commandsNames.add(cmd.getPrefix() + cmd.getName());
			}
			
			console.addCompleter(new StringsCompleter(commandsNames));
		}
	}
	
	private Shell buildShell() {
		TerminalIO io = new TerminalIO(console, error);

        List<String> path = new ArrayList<String>(1);
        path.add(prompt);

        MultiMap<String, Object> modifAuxHandlers = new ArrayHashMultiMap<String, Object>();
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
	
	/**
	 * Build the shell with the settings which you have set before.
	 * 
	 * @return A ready-to-use {@link Shell}.
	 */
	public Shell build(){
		try{
			checkPrecondition();
			configure();
			
			Shell shell = buildShell();
			configure(shell);
			
			return shell;
		}catch(IOException e){
			throw new RuntimeException("Could not build a shell!", e);
		}
	}
}