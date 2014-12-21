package de.raysha.lib.jsimpleshell.handler;

import java.lang.reflect.Method;
import java.util.Locale;

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
	 * @param description The description value of the command.
	 * @param targetMethod The target method.
	 * @return The resolved command description. Otherwise the <b>given</b> (included in the annotation) command description.
	 */
	public String resolveCommandDescription(String description, Method targetMethod);

	/**
	 * This method will be called if a command name should be resolved.
	 *
	 * @param name The name value of the command.
	 * @param targetMethod The target method.
	 * @return The resolved command name. Otherwise the <b>given</b> (included in the annotation) command name.
	 */
	public String resolveCommandName(String name, Method targetMethod);

	/**
	 * This method will be called if a command abbreviation should be resolved.
	 *
	 * @param abbrev The abbreviation value of the command.
	 * @param targetMethod The target method.
	 * @return The resolved command abbreviation. Otherwise the <b>given</b> (included in the annotation) command abbreviation.
	 */
	public String resolveCommandAbbrev(String abbrev, Method targetMethod);

	/**
	 * This method will be called if a command header should be resolved.
	 *
	 * @param header The header value of the command.
	 * @param targetMethod The target method.
	 * @return The resolved command header. Otherwise the <b>given</b> (included in the annotation) command header.
	 */
	public String resolveCommandHeader(String header, Method targetMethod);

	/**
	 * This method will be called if a parameter description should be resolved.
	 *
	 * @param description The description value of the parameter.
	 * @param targetMethod The target method.
	 * @return The resolved parameter description. Otherwise the <b>given</b> (included in the annotation) parameter description.
	 */
	public String resolveParamDescription(String description, Method targetMethod);

	/**
	 * This method will be called if a parameter name should be resolved.
	 *
	 * @param name The name value of the parameter.
	 * @param targetMethod The target method.
	 * @return The resolved parameter name. Otherwise the <b>given</b> (included in the annotation) parameter name.
	 */
	public String resolveParamName(String name, Method targetMethod);

	/**
	 * This method will be called if a general message should be resolved. For example: A general message can be the help-text and so on.
	 *
	 * @param message The message(-key).
	 * @return The resolved message. Otherwise the <b>given</b> message.
	 */
	public String resolveGeneralMessage(String message);

	/**
	 * Checks if this {@link MessageResolver} supports the given locale.
	 *
	 * @param locale Locale to check.
	 * @return True if the given locale will be supported. Otherwise false.
	 */
	public boolean supportsLocale(Locale locale);

	/**
	 * Set the {@link Locale} that should be used!
	 *
	 * @param locale The locale.
	 */
	public void setLocale(Locale locale);
}
