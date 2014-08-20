/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package de.raysha.lib.jsimpleshell.io;

import java.util.List;

import de.raysha.lib.jsimpleshell.PromptElement;

/**
 * Input provider for Shell.
 * Shell asks Input, "What does the user want to execute?", and Input reads and returns line from the user.
 * @author ASG
 */
public interface Input {

	String readCommand(List<PromptElement> path);

}
