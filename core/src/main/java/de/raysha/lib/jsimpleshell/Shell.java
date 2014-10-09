/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

/*
 *   Introducing the asg.cliche (http://cliche.sourceforge.net/)
 * Cliche is to be a VERY simple reflection-based command line shell
 * to provide simple CLI for simple applications.
 * The name formed as follows: "CLI Shell" --> "CLIShe" --> "Cliche".
 */

package de.raysha.lib.jsimpleshell;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.completer.AggregateCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.CandidatesChooser;
import de.raysha.lib.jsimpleshell.exception.AccessDeniedException;
import de.raysha.lib.jsimpleshell.exception.CLIException;
import de.raysha.lib.jsimpleshell.exception.ExitException;
import de.raysha.lib.jsimpleshell.exception.TokenException;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision.Decision;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.Context;
import de.raysha.lib.jsimpleshell.handler.CommandHookDependent;
import de.raysha.lib.jsimpleshell.handler.CommandHookDependent.ExecutionResult;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.handler.OutputDependent;
import de.raysha.lib.jsimpleshell.handler.ShellManageable;
import de.raysha.lib.jsimpleshell.handler.impl.CompositeCommandAccessManager;
import de.raysha.lib.jsimpleshell.handler.impl.CompositeMessageResolver;
import de.raysha.lib.jsimpleshell.handler.impl.DefaultMessageResolver;
import de.raysha.lib.jsimpleshell.io.Input;
import de.raysha.lib.jsimpleshell.io.InputBuilder;
import de.raysha.lib.jsimpleshell.io.InputConversionEngine;
import de.raysha.lib.jsimpleshell.io.Output;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;
import de.raysha.lib.jsimpleshell.io.OutputConversionEngine;
import de.raysha.lib.jsimpleshell.io.TerminalIO;
import de.raysha.lib.jsimpleshell.script.Environment;
import de.raysha.lib.jsimpleshell.util.ArrayHashMultiMap;
import de.raysha.lib.jsimpleshell.util.CommandChainInterpreter;
import de.raysha.lib.jsimpleshell.util.CommandChainInterpreter.CommandLine;
import de.raysha.lib.jsimpleshell.util.MultiMap;

/**
 * Shell is the class interacting with user.
 * Provides the command loop.
 * All logic lies here.
 *
 * @author ASG
 */
public class Shell {
	private OutputBuilder outputBuilder;
	private Output output;
	private InputBuilder inputBuilder;
	private Input input;
	private String appName;

	private final CompositeMessageResolver messageResolver;
	private final AggregateCandidatesChooser candidatesChooser;
	private DependencyResolver dependencyResolver;
	private final CompositeCommandAccessManager accessManager;
	private final Environment environment;

	/**
	 * Shell's constructor.
	 * <br />
	 * Use this constructor <b>for you own risk</b>! For building a shell see the
	 * {@link ShellBuilder}.
	 *
	 * @param settings Settings object for the shell instance
	 * @param initialHandlers The initial main handlers for this shell
	 * @param commandTable CommandTable to store commands
	 * @param path Shell's location: list of path elements.
	 * @param environment The shell's environment.
	 */
	public Shell(ShellSettings settings, Collection<Object> initialHandlers,
			CommandTable commandTable, List<PromptElement> path,
			Environment environment) {

		if(environment == null){
			throw new NullPointerException("The environment must not be null!");
		}

		this.commandTable = commandTable;
		this.path = path;

		this.environment = environment;
		this.messageResolver = configureMessageResolver(settings, initialHandlers);
		this.commandTable.setMessageResolver(messageResolver);

		this.accessManager = new CompositeCommandAccessManager(messageResolver);

		this.candidatesChooser = new AggregateCandidatesChooser();

		//pay attention! at this point all message resolver must be configured before!
		setSettings(settings, initialHandlers);

		enableExitCommand();
	}

	public ShellSettings getSettings() {
		return new ShellSettings(input, output, auxHandlers, displayTime);
	}

	private void setSettings(ShellSettings s, Collection<Object> initialHandlers) {
		input = s.getInput();
		output = s.getOutput();
		outputBuilder = new OutputBuilder(output);

		if(input instanceof TerminalIO){
			inputBuilder = new InputBuilder(((TerminalIO)input).getConsole());
		}

		dependencyResolver = configureDependencyResolver();

		displayTime = s.isDisplayTime();
		for (String prefix : s.getAuxHandlers().keySet()) {
			for (Object handler : s.getAuxHandlers().get(prefix)) {
				addAuxHandler(handler, prefix);
			}
		}
		for(Object handler : initialHandlers){
			addMainHandler(handler, "");
		}
		addMainHandler(environment, "");

		output.setMessageResolver(messageResolver);
	}

	private CompositeMessageResolver configureMessageResolver(
			ShellSettings settings, Collection<Object> initialHandlers) {

		CompositeMessageResolver messageResolver = new CompositeMessageResolver();
		messageResolver.setLocale(DefaultMessageResolver.getInstance().getLocale());

		for (String prefix : settings.getAuxHandlers().keySet()) {
			for (Object handler : settings.getAuxHandlers().get(prefix)) {
				addMessageResolver(messageResolver, handler);
			}
		}
		for(Object handler : initialHandlers){
			addMessageResolver(messageResolver, handler);
		}

		return messageResolver;
	}

	private void addMessageResolver(CompositeMessageResolver messageResolver,
			Object handler) {

		if(handler instanceof MessageResolver){
			List<MessageResolver> chain = messageResolver.getChain();
			if(!chain.contains(handler)){
				chain.add((MessageResolver)handler);
			}
		}
	}

	private CommandTable commandTable;

	/**
	 * @return the CommandTable for this shell.
	 */
	public CommandTable getCommandTable() {
		return commandTable;
	}

	private OutputConversionEngine outputConverter = new OutputConversionEngine();

	/**
	 * Call this method to get OutputConversionEngine used by the Shell.
	 * @return a conversion engine.
	 */
	public OutputConversionEngine getOutputConverter() {
		return outputConverter;
	}

	private InputConversionEngine inputConverter = new InputConversionEngine();

	/**
	 * Call this method to get InputConversionEngine used by the Shell.
	 * @return a conversion engine.
	 */
	public InputConversionEngine getInputConverter() {
		return inputConverter;
	}

	/**
	 * Get the environment of this shell.
	 *
	 * @return The environment.
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * Get the {@link CommandAccessManager} of this shell.
	 *
	 * @return The access manager.
	 */
	public CommandAccessManager getAccessManager() {
		return accessManager;
	}

	/**
	 * Get the {@link InputBuilder} of this shell.
	 *
	 * @return The input builder.
	 */
	public InputBuilder getInputBuilder() {
		return inputBuilder;
	}

	/**
	 * Get the {@link OutputBuilder} of this shell.
	 *
	 * @return The output builder.
	 */
	public OutputBuilder getOutputBuilder() {
		return outputBuilder;
	}

	/**
	 * Get the {@link MessageResolver} of this shell.
	 *
	 * @return The message resolver.
	 */
	public MessageResolver getMessageResolver() {
		return messageResolver;
	}

	/**
	 * Get the {@link CandidatesChooser} of this shell.
	 *
	 * @return The candidates chooser.
	 */
	public CandidatesChooser getCandidatesChooser() {
		return candidatesChooser;
	}

	private MultiMap<String, Object> auxHandlers = new ArrayHashMultiMap<String, Object>();
	private List<Object> allHandlers = new ArrayList<Object>();

	private DependencyResolver configureDependencyResolver() {
		DependencyResolver dependencyResolver = new DependencyResolver();

		dependencyResolver.put(this);
		dependencyResolver.put(getInputConverter());
		dependencyResolver.put(getOutputConverter());
		dependencyResolver.put(getInputBuilder());
		dependencyResolver.put(getOutputBuilder());
		dependencyResolver.put(getMessageResolver());
		dependencyResolver.put(getAccessManager());
		dependencyResolver.put(getEnvironment());

		return dependencyResolver;
	}

	/**
	 * Enable the exit command. By default it is enabled!
	 */
	public void enableExitCommand(){
		disableExitCommand();
		addMainHandler(new ExitCommand(), "");
	}

	/**
	 * Disable the exit command. Be careful! You must implements
	 * your own exit mechanism (@see ExitException). If you do not,
	 * the user can never exit the shell normally!
	 */
	public void disableExitCommand(){
		Iterator<Object> iter = allHandlers.iterator();

		while(iter.hasNext()){
			Object handler = iter.next();

			if(handler instanceof ExitCommand){
				commandTable.removeCommands(handler);
				iter.remove();
			}
		}
	}

	/**
	 * This is only a delegate to {@link OutputBuilder#disableColor()}.
	 * However, this method has the advantage that they effect the global
	 * {@link OutputBuilder}. That means that you don't have to disable the
	 * color on each time you get the {@link OutputBuilder} via {@link OutputDependent#cliSetOutput(OutputBuilder)}.
	 */
	public void disableColor() {
		outputBuilder.disableColor();
	}

	/**
	 * This is only a delegate to {@link OutputBuilder#enableColor()}.
	 * However, this method has the advantage that they effect the global
	 * {@link OutputBuilder}. That means that you don't have to enable the
	 * color on each time you get the {@link OutputBuilder} via {@link OutputDependent#cliSetOutput(OutputBuilder)}.
	 */
	public void enableColor() {
		outputBuilder.enableColor();
	}

	/**
	 * Method for registering command hanlers (or providers?)
	 * You call it, and from then the Shell has all commands declare in
	 * the handler object.
	 *
	 * This method recognizes if it is passed ShellDependent or ShellManageable
	 * and calls corresponding methods, as described in those interfaces.
	 *
	 * @see de.raysha.lib.jsimpleshell.handler.ShellDependent
	 * @see de.raysha.lib.jsimpleshell.handler.ShellManageable
	 *
	 * @param handler Object which should be registered as handler.
	 * @param prefix Prefix that should be prepended to all handler's command names.
	 */
	public void addMainHandler(Object handler, String prefix) {
		if (handler == null) {
			throw new NullPointerException();
		}
		allHandlers.add(handler);

		configureHandler(handler, prefix);
	}

	/**
	 * This method is very similar to addMainHandler, except ShellFactory
	 * will pass all handlers registered with this method to all this shell's subshells.
	 *
	 * @see de.raysha.lib.jsimpleshell.Shell#addMainHandler(java.lang.Object, java.lang.String)
	 *
	 * @param handler Object which should be registered as handler.
	 * @param prefix Prefix that should be prepended to all handler's command names.
	 */
	public void addAuxHandler(Object handler, String prefix) {
		if (handler == null) {
			throw new NullPointerException();
		}
		auxHandlers.put(prefix, handler);
		allHandlers.add(handler);

		configureHandler(handler, prefix);
	}

	private void configureHandler(Object handler, String prefix) {
		addDeclaredMethods(handler, prefix);
		inputConverter.addDeclaredConverters(handler);
		outputConverter.addDeclaredConverters(handler);

		dependencyResolver.resolveDependencies(handler);

		addMessageResolver(messageResolver, handler);
		if (handler instanceof CandidatesChooser) {
			candidatesChooser.addCandidatesChooser((CandidatesChooser)handler);
		}
		if (handler instanceof CommandAccessManager) {
			accessManager.addCommandAccessManager((CommandAccessManager)handler);
		}
	}

	private void addDeclaredMethods(Object handler, String prefix) throws SecurityException {
		for (Method m : handler.getClass().getMethods()) {
			Command annotation = m.getAnnotation(Command.class);
			if (annotation != null) {
				commandTable.addMethod(m, handler, prefix);
			}
		}
	}

	public void setMacroHome(File homeDir){
		if(homeDir == null){
			throw new NullPointerException("Home dir must not be null!");
		}

		if(!homeDir.exists() || !homeDir.isDirectory()){
			throw new IllegalArgumentException("The given file must exists and must be a directory!");
		}

		if(input instanceof TerminalIO){
			((TerminalIO) input).setMacroHome(homeDir);
		}
	}

	public File getMacroHome(){
		if(input instanceof TerminalIO){
			return new File(((TerminalIO) input).getMacroHome());
		}

		return null;
	}

	private Throwable lastException = null;

	/**
	 * Returns last thrown exception
	 */
	@Command(abbrev = "command.abbrev.lastexception", description = "command.description.lastexception",
			header = "command.header.lastexception", name = "command.name.lastexception") // Shell is self-manageable, isn't it?
	public Throwable getLastException() {
		return lastException;
	}

	@Command(abbrev = "command.abbrev.changelocale", description = "command.description.changelocale",
			header = "command.header.changelocale", name = "command.name.changelocale")
	public String changeLocale(
			@Param(value="param.name.changelocale", description="param.description.changelocale")
			Locale locale){

		if(messageResolver.supportsLocale(locale)){
			messageResolver.setLocale(locale);
			commandTable.refreshCommands();

			return messageResolver.resolveGeneralMessage("message.general.locale.changed");
		}else{
			return messageResolver.resolveGeneralMessage("message.general.locale.notsupported");
		}
	}

	private List<PromptElement> path;

	/**
	 * @return list of prompt elements, as it was passed in constructor
	 */
	public List<PromptElement> getPath() {
		return path;
	}

	/**
	 * Function to allow changing prompt at runtime; use with care to not break
	 * the semantics of sub-shells (if you're using them) or use to emulate
	 * tree navigation without subshells
	 * @param path New path
	 */
	public void setPath(List<PromptElement> path) {
		this.path = path;
	}

	/**
	 * Run the given script. This script should be a text file with including the
	 * executing commands. It is also possible to write comments in that file.
	 * Comments begins with the '#' character.
	 *
	 * @param script Script that should be executed.
	 * @param parameters Arguments for the script in form of: &lt;argName>=&lt;argValue>
	 * @throws CLIException
	 */
	public void runScript(File script, String...parameters) throws CLIException{
		if(script == null){
			throw new NullPointerException("The script must not be null!");
		}
		if(script.isDirectory()){
			throw new IllegalArgumentException("The script must be a file!");
		}
		if(!script.exists()){
			throw new IllegalArgumentException("The script doesn't exists!");
		}

		StringBuffer parameterLine = new StringBuffer("");
		for(String p : parameters){
			parameterLine.append(" \"");
			parameterLine.append(p);
			parameterLine.append("\"");
		}

		processLine("!run-script \"" + script.getAbsolutePath() + "\" " + parameterLine);
	}

	/**
	 * Run the given script. This script should be a text file with including the
	 * executing commands. It is also possible to write comments in that file.
	 * Comments begins with the '#' character.
	 *
	 * @param script Script that should be executed.
	 * @param parameters Arguments for the script
	 * @throws CLIException
	 */
	public void runScript(File script, Map<String, String> parameters) throws CLIException{
		if(script == null){
			throw new NullPointerException("The script must not be null!");
		}
		if(script.isDirectory()){
			throw new IllegalArgumentException("The script must be a file!");
		}
		if(!script.exists()){
			throw new IllegalArgumentException("The script doesn't exists!");
		}

		StringBuffer parameterLine = new StringBuffer("");
		for(Entry<String, String> p : parameters.entrySet()){
			parameterLine.append(" \"");
			parameterLine.append(p.getKey());
			parameterLine.append("=");
			parameterLine.append(p.getValue());
			parameterLine.append("\"");
		}

		processLine("!run-script \"" + script.getAbsolutePath() + "\" " + parameterLine);
	}

	/**
	 * Runs the command session.
	 * Create the Shell, then run this method to listen to the user,
	 * and the Shell will invoke Handler's methods.
	 * @throws java.io.IOException when can't readLine() from input.
	 */
	public void commandLoop() throws IOException {
		for (Object handler : allHandlers) {
			if (handler instanceof ShellManageable) {
				((ShellManageable)handler).cliEnterLoop(this);
			}
		}
		output.output(appName, outputConverter);
		String command = "";
		while (true) {
			try {
				command = input.readCommand(path);
				processLine(command);
			} catch(ExitException ee){
				if(ee.getMessage() != null){
					output.println(ee.getMessage());
				}
				break;
			} catch (CLIException e) {
				//do noting (proccessLine handle it for us)
			}
		}
		for (Object handler : allHandlers) {
			if (handler instanceof ShellManageable) {
				((ShellManageable)handler).cliLeaveLoop(this);
			}
		}
	}

	private void outputHeader(String header, Object[] parameters) {
		if (header == null || header.isEmpty()) {
			output.outputHeader(null);
		} else {
			output.outputHeader(String.format(header, parameters));
		}
	}

	private static final String HINT_FORMAT = "This is %1$s, running on JSimpleShell\n" +
			"For more information on the Shell, enter ?help";

	/**
	 * You can operate Shell linewise, without entering the command loop.
	 * All output is directed to shell's Output.
	 *
	 * @see de.raysha.lib.jsimpleshell.io.Output
	 *
	 * @param line Full command line
	 * @throws de.raysha.lib.jsimpleshell.exception.CLIException This may be TokenException
	 */
	public void processLine(String line) throws CLIException {
		if (line.trim().equals("?")) {
			output.output(String.format(HINT_FORMAT, appName), outputConverter);
		} else {
			List<CommandLine> chain = CommandChainInterpreter.interpretTokens(Token.tokenize(line));

			if (chain.size() > 0) {
				for(CommandLine commandLine : chain){
					try{
						if(commandLine.isOr() || lastException == null){
							lastException = null;
							processLine(commandLine.getTokens());
						}else if(commandLine.isAnd() && lastException != null){
							break;
						}
					} catch (TokenException te) {
						lastException = te;
						output.outputException(line, te);
					} catch(ExitException ee){
						throw ee;
					} catch (CLIException clie) {
						lastException = clie;
						output.outputException(clie);
					}
				}

				if(lastException != null) {
					throw (CLIException)lastException;
				}
			}
		}
	}

	private void processLine(List<Token> tokens) throws CLIException {
		if(!tokens.isEmpty()){
			String discriminator = tokens.get(0).getString();
			processCommand(discriminator, tokens);
		}
	}

	private void processCommand(String discriminator, List<Token> tokens) throws CLIException {
		assert discriminator != null;
		assert ! discriminator.equals("");

		ShellCommand commandToInvoke = commandTable.lookupCommand(discriminator, tokens, inputConverter);

		Class<?>[] paramClasses = commandToInvoke.getMethod().getParameterTypes();
		Object[] parameters = inputConverter.convertToParameters(tokens,
				commandToInvoke.getParamSpecs(), paramClasses,
				commandToInvoke.getMethod().isVarArgs());

		checkAccess(commandToInvoke, parameters);
		outputHeader(commandToInvoke.getHeader(), parameters);
		informHooks(commandToInvoke, parameters);

		long timeBefore = System.currentTimeMillis();
		CLIException thrown = null;
		Object invocationResult = null;

		try {
			invocationResult = commandToInvoke.invoke(parameters);
		} catch (CLIException e) {
			thrown = e;
		}

		long timeAfter = System.currentTimeMillis();
		final long time = timeAfter - timeBefore;

		informHooks(commandToInvoke, parameters, invocationResult, thrown, time);

		if (displayTime) {
			if (time != 0L) {
				output.output(String.format(TIME_MS_FORMAT_STRING, time), outputConverter);
			}
		}

		if (invocationResult != null && commandToInvoke.isDisplayResult()) {
			output.output(invocationResult, outputConverter);
		}
		if(thrown != null){
			if(thrown.getCause() instanceof ExitException){
				throw (ExitException)thrown.getCause();
			}

			throw thrown;
		}
	}

	private void checkAccess(ShellCommand commandToInvoke, Object[] parameters) throws AccessDeniedException {
		final Context context = new Context(commandToInvoke, parameters);
		final AccessDecision decision = accessManager.checkCommandPermission(context);

		if(decision.getDecision() == Decision.DENIED){
			informHooks(commandToInvoke, parameters, decision);

			if(decision.getReason() != null){
				throw new AccessDeniedException(decision.getReason());
			}else{
				throw new AccessDeniedException();
			}
		}
	}

	private void informHooks(ShellCommand commandToInvoke, Object[] parameters, AccessDecision decision) {
		for(Object handler : allHandlers){
			if(handler instanceof CommandHookDependent){
				((CommandHookDependent)handler).cliDeniedCommand(commandToInvoke, parameters, decision);
			}
		}
	}

	private void informHooks(ShellCommand commandToInvoke, Object[] parameters) {
		for(Object handler : allHandlers){
			if(handler instanceof CommandHookDependent){
				((CommandHookDependent)handler).cliBeforeCommand(commandToInvoke, parameters);
			}
		}
	}

	private void informHooks(ShellCommand commandToInvoke, Object[] parameters, Object invocationResult, CLIException thrown, long time) {
		for(Object handler : allHandlers){
			if(handler instanceof CommandHookDependent){
				ExecutionResult result = thrown != null ?
						new ExecutionResult(thrown.getCause(), time) :
						new ExecutionResult(invocationResult, time);

				((CommandHookDependent)handler).cliAfterCommand(commandToInvoke, parameters, result);
			}
		}
	}

	private static final String TIME_MS_FORMAT_STRING = "time: %d ms";

	private boolean displayTime = false;

	/**
	 * Turns command execution time display on and off
	 * @param displayTime true if do display, false otherwise
	 */
	@Command(abbrev = "command.abbrev.displaytime", description = "command.description.displaytime",
			header = "command.header.displaytime", name = "command.name.displaytime")
	public void setDisplayTime(
			@Param(value="param.name.displaytime", description="param.description.displaytime")
			boolean displayTime) {
		this.displayTime = displayTime;
	}


	/**
	 * Hint is some text displayed before the command loop and every time user enters "?".
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppName() {
		return appName;
	}

	private class ExitCommand {

		@Command(abbrev = "command.abbrev.exit", description = "command.description.exit",
				header = "command.header.exit", name = "command.name.exit")
		public void exit() throws ExitException{
			throw new ExitException();
		}
	}
}
