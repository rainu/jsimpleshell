package de.raysha.lib.jsimpleshell.example;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.raysha.lib.jsimpleshell.handler.InputConverter;

/**
 * This class is responsible for converting a (user) input-string.
 *
 * @author rainu
 */
public class DateInputConverter implements InputConverter {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public Object convertInput(String original, Class<?> toClass) throws Exception {
		if(!responsibleFor(toClass)){
			//if i am not responsible for, i must return null!
			return null;
		}

		//return the final converted object
		return sdf.parse(original);
	}

	/*
	 * As an InputConverter i will get ALL (user) input strings.
	 * But i am not responsible for all target classes!
	 */
	private boolean responsibleFor(Class<?> toClass) {
		return Date.class == toClass;
	}

}
