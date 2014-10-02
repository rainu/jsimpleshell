/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package de.raysha.lib.jsimpleshell.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

import de.raysha.lib.jsimpleshell.completer.BooleanCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.CandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.CommandNameCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.FileCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.LocaleCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.MacroNameCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.VariableCandidatesChooser;

/**
 * Annotation for parameters of Command-marked methods.
 * This annotation is of particular usefullness, because Java 5 Reflection doesn't have access
 * to declared parameter names (there's simply no such information stored in classfile).
 * You must at least provide name attribute, others being optional.
 * @author ASG
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
	/**
	 * Parameter name.
	 * Should (1) reflect the original Java parameter name, (2) be short and descriptive to the user.
	 * Recommendations: "number-of-nodes", "user-login", "coefficients".
	 * @return The name ascribed to annotated method parameter.
	 */
	String value() default "";

	/**
	 * One-sentence description of the parameter.
	 * It is recommended that you always set it.
	 * @return "Short description attribute" of the annotated parameter.
	 */
	String description() default "";

	/**
	 * Specify a custom type for the parameter. It can be used by a {@link CandidatesChooser}
	 * to determine the special type. See {@link DefaultTypes} for a list a´of a list of
	 * types that are available by default.
	 *
	 * @return The custom type for that parameter.
	 */
	String type() default "";

	public static abstract class DefaultTypes {
		/**
		 * Type for a {@link Boolean} parameter.
		 */
		public static final String BOOLEAN = BooleanCandidatesChooser.BOOLEAN_TYPE;

		/**
		 * Type for a (available) command name.
		 */
		public static final String COMMAND_NAME = CommandNameCandidatesChooser.COMMAND_NAME_TYPE;

		/**
		 * Type for files (includes directories).
		 */
		public static final String FILE = FileCandidatesChooser.FILES_TYPE;

		/**
		 * Type for directories.
		 */
		public static final String DIRECTORY = FileCandidatesChooser.DIRECTORY_ONLY_TYPE;

		/**
		 * Type for {@link Locale}.
		 */
		public static final String LOCALE = LocaleCandidatesChooser.LOCALE_TYPE;

		/**
		 * Type for a macro name.
		 */
		public static final String MACRO_NAME = MacroNameCandidatesChooser.MACRO_NAME_TYPE;

		/**
		 * Type for a variable name in the current environment.
		 */
		public static final String VARIABLE = VariableCandidatesChooser.VARIABLE_NAME_TYPE;
	}
}
