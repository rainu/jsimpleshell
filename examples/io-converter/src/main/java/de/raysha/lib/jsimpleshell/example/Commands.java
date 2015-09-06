package de.raysha.lib.jsimpleshell.example;

import java.util.Date;

import de.raysha.lib.jsimpleshell.annotation.Command;

public class Commands {

	@Command
	public Date now(){
		/*
		 * The DateOutputConverter is responsible for
		 * converting this Date into printable-date.
		 */
		return new Date();
	}

	/*
	 * The DateInputConverter is responsible for converting
	 * the user input into the date. If the converter could not
	 * convert the input string, this command will be not called!
	 */
	@Command
	public Long date(Date date){
		return date.getTime();
	}
}
