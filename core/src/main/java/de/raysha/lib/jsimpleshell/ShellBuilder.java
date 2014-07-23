package de.raysha.lib.jsimpleshell;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import jline.console.ConsoleReader;
import jline.console.UserInterruptException;
import jline.console.completer.Completer;
import jline.console.history.FileHistory;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.io.InputConverter;
import de.raysha.lib.jsimpleshell.io.InputDependent;
import de.raysha.lib.jsimpleshell.io.OutputConverter;
import de.raysha.lib.jsimpleshell.io.OutputDependent;
import de.raysha.lib.jsimpleshell.io.TerminalIO;
import de.raysha.lib.jsimpleshell.util.ArrayHashMultiMap;
import de.raysha.lib.jsimpleshell.util.MultiMap;

/**
 * This is a class that can be used to build a {@link Shell}.
 * 
 * @author rainu
 */
public class ShellBuilder {
	private String prompt;
	private String appName = null;
	private MultiMap<String, Object> auxHandlers = new ArrayHashMultiMap<String, Object>();
	private Collection<Object> handlers = new LinkedList<Object>();
	private File history;
	private Shell parent;
	private ConsoleReader console;
	private OutputStream error = System.err;
	private boolean useForeignConsole = false;
	private boolean fileNameCompleterEnabled = true;
	private boolean commandCompleterEnabled = true;
	private boolean handleUserInterrupt = false;
	
	private ShellBuilder(){ }
	
	/**
	 * Create a new {@link ShellBuilder} instance with which one you can build a {@link Shell}.
	 * 
	 * @param prompt The promt for this shell.
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
	 * Set the app name. This name will be shown if the shell will be entered.
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
     * This method is very similar to addHandler, except ShellBuilder
     * will pass all handlers registered with this method to all this shell's subshells.
     *
     * @see de.raysha.lib.jsimpleshell.ShellBuilder#addHandler(java.lang.Object)
     *
     * @param handler Object which should be registered as handler.
     * @return This {@link ShellBuilder}
     */
	public ShellBuilder addAuxHandler(Object handler){
		auxHandlers.put("", handler);
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
	 * Should the shell handle user interrupts (CRTL-C)?
	 * 
	 * @param handleUserInterrupt
	 *            False will cause that the JVM will handle SIGINT as normal, which usually
	 *            causes it to exit. True will cause that the JVM will not handle the signal.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setHandleUserInterrupt(boolean handleUserInterrupt) {
		this.handleUserInterrupt = handleUserInterrupt;
		
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
	
	/**
	 * Set the location of the history. By default the history will be not persisted!
	 * 
	 * @param historyFile The location of the history. Null means that the history will not persisted.
	 * @return This {@link ShellBuilder}
	 */
	public ShellBuilder setHistoryFile(File historyFile){
		this.history = historyFile;
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
				boolean alreadyAdded = false;
				for(Completer c : console.getCompleters()){
					if(c instanceof FileArgumentCompleter){
						alreadyAdded = true;
						break;
					}
				}
				
				if(!alreadyAdded){
					console.addCompleter(new FileArgumentCompleter());
				}
			}
			
			if(history != null && !(console.getHistory() instanceof FileHistory)){
				try {
					console.setHistory(new FileHistory(history));
				} catch (IOException e) {
					throw new RuntimeException("Could not configure file history.", e);
				}
				
				if(auxHandlers.get("historyFlusher").isEmpty()){
					auxHandlers.put("historyFlusher", new HistoryFlusher());
				}
			}
			
			console.setHandleUserInterrupt(handleUserInterrupt);
		}
		console.setExpandEvents(false);
	}
	
	private Shell buildShell() {
		TerminalIO io = new TerminalIO(console, error);

        List<String> path = new ArrayList<String>(1);
        path.add(prompt);

        MultiMap<String, Object> modifAuxHandlers = new ArrayHashMultiMap<String, Object>(auxHandlers);
        modifAuxHandlers.put("!", io);

        Shell theShell = new Shell(new Shell.Settings(io, io, modifAuxHandlers, false),
                new CommandTable(new DashJoinedNamer(true)), path);
        
        configureShell(theShell);
        return theShell;
	}

	private Shell buildSubShell() {
		List<String> newPath = new ArrayList<String>(parent.getPath());
        newPath.add(prompt);

        Shell subshell = new Shell(parent.getSettings().createWithAddedAuxHandlers(auxHandlers),
                new CommandTable(parent.getCommandTable().getNamer()), newPath);

        configureShell(subshell);
        return subshell;
	}
	
	private void configureShell(Shell shell) {
		shell.setAppName(appName);
		shell.addMainHandler(shell, "!");
        shell.addMainHandler(new HelpCommandHandler(), "?");
        
        if(commandCompleterEnabled){
        	shell.addMainHandler(new CommandCompleterHandler(), "");
        }
        
        for (Object h : handlers) {
            shell.addMainHandler(h, "");
        }
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
			
			return shell;
		}catch(IOException e){
			throw new RuntimeException("Could not build a shell!", e);
		}
	}
}