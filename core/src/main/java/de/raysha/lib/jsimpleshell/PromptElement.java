package de.raysha.lib.jsimpleshell;


/**
 * Each (Sub-){@link Shell} have one {@link PromptElement}. Whenever the prompt should be displayed
 * on the terminal, this {@link PromptElement} will be rendered. The result of the rendering will
 * be finally printed out. 
 * 
 * @author rainu
 *
 */
public interface PromptElement {

	/**
	 * This method is called whenever the prompt should be printed.
	 * 
	 * @return The string representation. This will be finally printed out.
	 */
	public String render();
}
