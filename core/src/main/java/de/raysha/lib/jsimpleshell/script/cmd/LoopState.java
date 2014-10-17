package de.raysha.lib.jsimpleshell.script.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.script.cmd.LoopCommandHandler.LoopSpecialCommand;
import de.raysha.lib.jsimpleshell.script.cmd.LoopState.LoopExecution;

/**
 * This class holds all informations about a loop-body (recorded commands).
 *
 * @author rainu
 */
class LoopState implements Iterator<LoopExecution>{
	private static long GLOBAL_ID_COUNTER = 0L;
	private final long uniqId = GLOBAL_ID_COUNTER++;

	private List<String> commands;
	private final Iterator<? extends Object> iterator;
	private final Map<String, ? extends Object> staticVariables;
	private final String stateVariableName;

	private final MessageResolver messageResolver;

	/**
	 * Creates a {@link LoopState}.
	 *
	 * @param iterator The loop's {@link Iterator}
	 * @param staticVariables A map with variables that should be available in the loop environment
	 * @param stateVariableName The name for the variable (environment) that contains the continues state of the loop
	 * @param messageResolver The currently used {@link MessageResolver}
	 */
	@SuppressWarnings("unchecked")
	public LoopState(
			Iterator<? extends Object> iterator,
			Map<String, ? extends Object> staticVariables,
			String stateVariableName,
			MessageResolver messageResolver){

		this.staticVariables = (Map<String, ? extends Object>) (staticVariables == null ? Collections.emptyMap() : staticVariables);
		this.iterator = iterator;
		this.stateVariableName = stateVariableName;
		this.messageResolver = messageResolver;
	}

	/**
	 * Creates a {@link LoopState}.
	 *
	 * @param recordedCommands The recorded commands of the user (these are the loop-body)
	 * @param iterator The loop's {@link Iterator}
	 * @param staticVariables A map with variables that should be available in the loop environment
	 * @param stateVariableName The name for the variable (environment) that contains the continues state of the loop
	 * @param messageResolver The currently used {@link MessageResolver}
	 */
	public LoopState(
			List<String> recordedCommands,
			Iterator<? extends Object> iterator,
			Map<String, ? extends Object> staticVariables,
			String stateVariableName,
			MessageResolver messageResolver){

		this(iterator, staticVariables, stateVariableName, messageResolver);
		setCommands(recordedCommands);
	}

	public void setCommands(List<String> commands) {
		this.commands = Collections.unmodifiableList(new ArrayList<String>(commands));
	}

	public String getStateVariableName() {
		return stateVariableName;
	}

	public Map<String, ? extends Object> getStaticVariables() {
		return Collections.unmodifiableMap(staticVariables);
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public void remove() {
		iterator.remove();
	}

	@Override
	public LoopExecution next() {
		final Object next = iterator.next();

		return new LoopExecution(next);
	}

	public long getUniqId() {
		return uniqId;
	}

	public class LoopExecution {
		private final Object currentItem;

		public LoopExecution(Object currentItem) {
			this.currentItem = currentItem;
		}

		public List<String> getCommands(){
			return enrichCommands(commands);
		}

		private List<String> enrichCommands(List<String> userCommands) {
			LinkedList<String> enriched = new LinkedList<String>();
			final List<String> setEnvironmentCommands = generateSetEnvironmentVariables();
			final List<String> removeEnvironmentCommands = generateRemoveEnvironmentVariables();

			enriched.addAll(setEnvironmentCommands);

			//if an inner-loop is created.. the loop variables will be overridden by the inner loop
			//we must restore the variables after the inner loop is leaving
			for(int i=0; i < userCommands.size(); i++){
				final String command = userCommands.get(i);
				enriched.add(command);

				if(	command.contains(getCommandNameForLoopEnd()) ||
					command.contains(getCommandAbbrevForLoopEnd())){

					enriched.addAll(setEnvironmentCommands);
				}
			}

			enriched.addAll(removeEnvironmentCommands);

			return enriched;
		}

		private List<String> generateSetEnvironmentVariables() {
			List<String> commands = new ArrayList<String>(staticVariables.size() + 1);

			//continues variable
			commands.add(setGlobalVariable(stateVariableName, currentItem));

			for(Entry<String, ? extends Object> entry : staticVariables.entrySet()){
				final String command = setGlobalVariable(entry.getKey(), entry.getValue());
				commands.add(command);
			}

			return commands;
		}

		private List<String> generateRemoveEnvironmentVariables() {
			List<String> commands = new ArrayList<String>(staticVariables.size() + 1);

			//continues variable
			commands.add(removeGlobalVariable(stateVariableName));

			for(String varName : staticVariables.keySet()){
				final String command = removeGlobalVariable(varName);
				commands.add(command);
			}

			return commands;
		}

		private String setGlobalVariable(String varName, Object value) {
			final String command = "." + messageResolver.resolveGeneralMessage(EnvironmentCommandHandler.COMMAND_NAME_SET_GLOBAL_VARIABLE);

			return command + " \"" + varName + "\" \"" + String.valueOf(value) + "\"";
		}

		private String removeGlobalVariable(String name) {
			//TODO: implement remove variables!
			return "";
		}

		private String getCommandAbbrevForLoopEnd() {
			return "." + messageResolver.resolveGeneralMessage(LoopSpecialCommand.COMMAND_ABBREV_LOOP_END);
		}

		private String getCommandNameForLoopEnd() {
			return "." + messageResolver.resolveGeneralMessage(LoopSpecialCommand.COMMAND_NAME_LOOP_END);
		}
	}
}
