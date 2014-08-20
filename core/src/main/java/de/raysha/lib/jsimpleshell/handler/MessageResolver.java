package de.raysha.lib.jsimpleshell.handler;

import java.lang.reflect.Method;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;

/**
 * Classes which implements that interface can resolve
 * {@link Command}/{@link Param} values.
 *
 * @author rainu
 */
public interface MessageResolver {

	/**
	 * This method will be called if a command description should be resolved.
	 *
	 * @param command The annotation that contains the description.
	 * @param annotatedMethod The annotated method.
	 * @return The resolved command description. Otherwise the <b>given</b> (included in the annotation) command description.
	 */
	public String resolveCommandDescription(Command command, Method annotatedMethod);

	/**
	 * This method will be called if a command name should be resolved.
	 *
	 * @param command The annotation that contains the name.
	 * @param annotatedMethod The annotated method.
	 * @return The resolved command name. Otherwise the <b>given</b> (included in the annotation) command name.
	 */
	public String resolveCommandName(Command command, Method annotatedMethod);

	/**
	 * This method will be called if a command abbreviation should be resolved.
	 *
	 * @param command The annotation that contains the abbreviation.
	 * @param annotatedMethod The annotated method.
	 * @return The resolved command abbreviation. Otherwise the <b>given</b> (included in the annotation) command abbreviation.
	 */
	public String resolveCommandAbbrev(Command command, Method annotatedMethod);

	/**
	 * This method will be called if a command header should be resolved.
	 *
	 * @param command The annotation that contains the header.
	 * @param annotatedMethod The annotated method.
	 * @return The resolved command header. Otherwise the <b>given</b> (included in the annotation) command header.
	 */
	public String resolveCommandHeader(Command command, Method annotatedMethod);

	/**
	 * This method will be called if a parameter description should be resolved.
	 *
	 * @param param The annotation that contains the description.
	 * @param annotatedMethod The annotated method.
	 * @return The resolved parameter description. Otherwise the <b>given</b> (included in the annotation) parameter description.
	 */
	public String resolveParamDescription(Param param, Method annotatedMethod);

	/**
	 * This method will be called if a parameter name should be resolved.
	 *
	 * @param param The annotation that contains the name.
	 * @param annotatedMethod The annotated method.
	 * @return The resolved parameter name. Otherwise the <b>given</b> (included in the annotation) parameter name.
	 */
	public String resolveParamName(Param param, Method annotatedMethod);

	/**
	 * This method will be called if a general message should be resolved. For example: A general message can be the help-text and so on.
	 *
	 * @param message The message(-key).
	 * @return The resolved message. Otherwise the <b>given</b> message.
	 */
	public String resolveGeneralMessage(String message);

}
