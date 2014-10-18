package de.raysha.lib.jsimpleshell.util;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;

/**
 * This builder can be used for building a colored string.
 * That means you can easily build a string that contains the ANSI color codes.
 *
 * @author rainu
 *
 */
public class ColoredStringBuilder {
	private StringBuffer sb = new StringBuffer();

	/**
	 * Add a text without color.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.normal", description = "command.description.echo.builder.normal",
			header = "command.header.echo.builder.normal", name = "command.name.echo.builder.normal", displayResult = false)
	public ColoredStringBuilder normal(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append(concat(values));

		return this;
	}

	/**
	 * Add a <b>black-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.black", description = "command.description.echo.builder.black",
			header = "command.header.echo.builder.black", name = "command.name.echo.builder.black", displayResult = false)
	public ColoredStringBuilder black(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[30m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>red-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.red", description = "command.description.echo.builder.red",
			header = "command.header.echo.builder.red", name = "command.name.echo.builder.red", displayResult = false)
	public ColoredStringBuilder red(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[31m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>green-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.green", description = "command.description.echo.builder.green",
			header = "command.header.echo.builder.green", name = "command.name.echo.builder.green", displayResult = false)
	public ColoredStringBuilder green(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[32m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>yellow-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.yellow", description = "command.description.echo.builder.yellow",
			header = "command.header.echo.builder.yellow", name = "command.name.echo.builder.yellow", displayResult = false)
	public ColoredStringBuilder yellow(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[33m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>blue-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.blue", description = "command.description.echo.builder.blue",
			header = "command.header.echo.builder.blue", name = "command.name.echo.builder.blue", displayResult = false)
	public ColoredStringBuilder blue(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[34m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>magenta-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.magenta", description = "command.description.echo.builder.magenta",
			header = "command.header.echo.builder.magenta", name = "command.name.echo.builder.magenta", displayResult = false)
	public ColoredStringBuilder magenta(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[35m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>cyan-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.cyan", description = "command.description.echo.builder.cyan",
			header = "command.header.echo.builder.cyan", name = "command.name.echo.builder.cyan", displayResult = false)
	public ColoredStringBuilder cyan(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[36m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>white-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.white", description = "command.description.echo.builder.white",
			header = "command.header.echo.builder.white", name = "command.name.echo.builder.white", displayResult = false)
	public ColoredStringBuilder white(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[37m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>black-background-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.black.bg", description = "command.description.echo.builder.black.bg",
			header = "command.header.echo.builder.black.bg", name = "command.name.echo.builder.black.bg", displayResult = false)
	public ColoredStringBuilder blackBG(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[40m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>red-background-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.red.bg", description = "command.description.echo.builder.red.bg",
			header = "command.header.echo.builder.red.bg", name = "command.name.echo.builder.red.bg", displayResult = false)
	public ColoredStringBuilder redBG(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[41m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>green-background-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.green.bg", description = "command.description.echo.builder.green.bg",
			header = "command.header.echo.builder.green.bg", name = "command.name.echo.builder.green.bg", displayResult = false)
	public ColoredStringBuilder greenBG(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[42m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>yellow-background-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.yellow.bg", description = "command.description.echo.builder.yellow.bg",
			header = "command.header.echo.builder.yellow.bg", name = "command.name.echo.builder.yellow.bg", displayResult = false)
	public ColoredStringBuilder yellowBG(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[43m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>blue-background-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.blue.bg", description = "command.description.echo.builder.blue.bg",
			header = "command.header.echo.builder.blue.bg", name = "command.name.echo.builder.blue.bg", displayResult = false)
	public ColoredStringBuilder blueBG(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[44m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>magenta-background-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.magenta.bg", description = "command.description.echo.builder.magenta.bg",
			header = "command.header.echo.builder.magenta.bg", name = "command.name.echo.builder.magenta.bg", displayResult = false)
	public ColoredStringBuilder magentaBG(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[45m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>cyan-background-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.cyan.bg", description = "command.description.echo.builder.cyan.bg",
			header = "command.header.echo.builder.cyan.bg", name = "command.name.echo.builder.cyan.bg", displayResult = false)
	public ColoredStringBuilder cyanBG(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[46m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>white-background-colored</b> text.
	 *
	 * @param values
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.white.bg", description = "command.description.echo.builder.white.bg",
			header = "command.header.echo.builder.white.bg", name = "command.name.echo.builder.white.bg", displayResult = false)
	public ColoredStringBuilder whiteBG(
			@Param(value = "param.name.echo", description = "param.description.echo")
			Object...values){

		sb.append("\u001B[47m" + concat(values) + "\u001B[0m");

		return this;
	}

	/**
	 * Cause that all previous entered text will be cleared.
	 *
	 * @return This {@link ColoredStringBuilder}.
	 */
	@Command(abbrev = "command.abbrev.echo.builder.clear", description = "command.description.echo.builder.clear",
			header = "command.header.echo.builder.clear", name = "command.name.echo.builder.clear", displayResult = false)
	public ColoredStringBuilder clear(){
		sb = new StringBuffer();

		return this;
	}

	private String concat(Object...values){
		StringBuilder result = new StringBuilder();

		for(Object value : values){
			result.append(value);
		}

		return result.toString();
	}

	public String build(){
		return sb.toString();
	}
}
