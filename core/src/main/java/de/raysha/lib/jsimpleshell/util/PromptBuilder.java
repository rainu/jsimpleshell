package de.raysha.lib.jsimpleshell.util;

import java.util.ArrayList;
import java.util.List;

import de.raysha.lib.jsimpleshell.PromptElement;

/**
 * THis class is responsible for building some basic {@link PromptElement}s.
 * 
 * @author rainu
 *
 */
public class PromptBuilder {

	public static String joinPromptElements(List<PromptElement> elements, boolean fixCase, char withChar){
		List<String> rendered = new ArrayList<String>();
		
		for(PromptElement pe : elements){
			rendered.add(pe.render());
		}
		
		return Strings.joinStrings(rendered, fixCase, withChar);
	}

	/**
	 * Build a {@link PromptElement} that looks like the given string.
	 * 
	 * @param prompt String representation of the prompt.
	 * @return The {@link PromptElement} which render the given prompt.
	 */
	public static PromptElement fromString(final String prompt) {
		return new PromptElement() {
			@Override
			public String render() {
				return prompt;
			}
		};
	}
}
