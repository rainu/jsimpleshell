package de.raysha.lib.jsimpleshell.example;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.raysha.lib.jsimpleshell.handler.OutputConverter;

/**
 * This class is responsible for convert {@link Date}-Object to printable String.
 *
 * @author rainu
 */
public class DateOutputConverter implements OutputConverter {

	private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

	@Override
	public Object convertOutput(Object toBeFormatted) {
		if(!responsibleFor(toBeFormatted)){
			//if i am not responsible for, i must return null!
			return null;
		}

		return sdf.format((Date)toBeFormatted);
	}

	/*
	 * As an OutputConverter i will get ALL output objects.
	 * But i am not responsible for all of them!
	 */
	private boolean responsibleFor(Object toBeFormatted) {
		return toBeFormatted instanceof Date;
	}

}
