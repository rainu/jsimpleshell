package de.raysha.lib.jsimpleshell;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a pipeline of command-lines. All command lines will be
 * execute <b>before<b> the user can enter a new one.
 *
 * @author rainu
 */
public class CommandPipeline {
	private LinkedList<String> pipeline = new LinkedList<String>();

	/**
	 * Checks if the pipeline have more command-lines.
	 *
	 * @return True if the one or more commands are available. Otherwise false.
	 */
	public boolean hasNext() {
		return !pipeline.isEmpty();
	}

	/**
	 * Append one or many command lines into the pipeline.
	 *
	 * @param commands The list of command lines.
	 */
	public void append(List<String> commands) {
		if(commands != null) for(String command : commands){
			pipeline.addLast(command);
		}
	}

	/**
	 * Append one or many command lines into the pipeline.
	 *
	 * @param commands The array of command lines.
	 */
	public void append(String...commands) {
		append(Arrays.asList(commands));
	}

	/**
	 * Prepend one or many command lines into the pipeline.
	 *
	 * @param commands The list of command lines.
	 */
	public void prepend(List<String> commands) {
		if(commands != null) for(int i = commands.size() - 1; i >= 0; i--){
			pipeline.addFirst(commands.get(i));
		}
	}

	/**
	 * Prepend one or many command lines into the pipeline.
	 *
	 * @param commands The array of command lines.
	 */
	public void prepend(String...commands) {
		prepend(Arrays.asList(commands));
	}

	/**
	 * Get and remove the next command-line in this pipeline.
	 *
	 * @return The next command-line.
	 */
	public String pop() {
		return pipeline.removeFirst();
	}

}
