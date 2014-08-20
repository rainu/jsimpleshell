package de.raysha.lib.jsimpleshell.util;

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
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder normal(Object obj){
		sb.append(obj);

		return this;
	}

	/**
	 * Add a <b>black-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder black(Object obj){
		sb.append("\u001B[30m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>red-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder red(Object obj){
		sb.append("\u001B[31m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>green-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder green(Object obj){
		sb.append("\u001B[32m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>yellow-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder yellow(Object obj){
		sb.append("\u001B[33m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>blue-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder blue(Object obj){
		sb.append("\u001B[34m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>magenta-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder magenta(Object obj){
		sb.append("\u001B[35m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>cyan-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder cyan(Object obj){
		sb.append("\u001B[36m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>white-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder white(Object obj){
		sb.append("\u001B[37m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>black-background-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder blackBG(Object obj){
		sb.append("\u001B[40m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>red-background-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder redBG(Object obj){
		sb.append("\u001B[41m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>green-background-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder greenBG(Object obj){
		sb.append("\u001B[42m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>yellow-background-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder yellowBG(Object obj){
		sb.append("\u001B[43m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>blue-background-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder blueBG(Object obj){
		sb.append("\u001B[44m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>magenta-background-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder magentaBG(Object obj){
		sb.append("\u001B[45m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>cyan-background-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder cyanBG(Object obj){
		sb.append("\u001B[46m" + obj + "\u001B[0m");

		return this;
	}

	/**
	 * Add a <b>white-background-colored</b> text.
	 *
	 * @param obj
	 * @return This {@link ColoredStringBuilder}.
	 */
	public ColoredStringBuilder whiteBG(Object obj){
		sb.append("\u001B[47m" + obj + "\u001B[0m");

		return this;
	}

	public String build(){
		return sb.toString();
	}
}
