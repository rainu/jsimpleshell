/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package de.raysha.lib.jsimpleshell.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.raysha.lib.jsimpleshell.CommandRecorder;

/**
 * Annotation for commands. Allows to specify the name of a command, otherwise method's name is used.
 * @author ASG
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/**
	 * Allows to override default command name, which is derived from method's name
	 * @return "" or null if default name is used, user-specified name otherwise.
	 */
	String value() default ""; // if "" then Null is assumed.

	/**
	 * It is only a alternative for value.
	 * @see Command#value()
	 */
	String name() default ""; // if "" then Null is assumed.

	/**
	 * Specify the description of the command. Default description (if this
	 * property is not set) says "methodName(Arg1Type, Arg2Type,...) : ReturnType".
	 * @return command's description or "" if not set.
	 */
	String description() default "";

	/**
	 * Specify the shortcut name for the command.
	 * If not set, if the name attribute is not set as well, the Shell takes
	 * the first letter of each word (void selectUser() --- select-user --- su).
	 * @return command's abbreviation or "" if not set.
	 */
	String abbrev() default "";

	/**
	 * Specify the string to output before command's output, i.e. some explanations.
	 * @return command's header or "" if not set.
	 */
	String header() default "";

	/**
	 * Specify if the result of the command should be displayed.
	 * @return True if the result should be displayed. Otherwise false.
	 */
	boolean displayResult() default true;

	/**
	 * Specify if the command starts a subshell. This information is needed
	 * by the {@link CommandRecorder}. If a command starts a subshell and does not
	 * define it, the {@link CommandRecorder} will <b>not enter</b> in the subshell. The
	 * result is, that the user can not enter the subshell if he wants to create
	 * a loop or something like that. Than the user have no chance for auto-complete
	 * the commands in the subshell.
	 *
	 * @return True if the command (can) starts a subshell. Otherwise false.
	 */
	boolean startsSubshell() default false;
}
