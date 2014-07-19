package de.rainu.lib.jsimpleshell.example;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.rainu.lib.jsimpleshell.annotation.Command;
import de.rainu.lib.jsimpleshell.annotation.Param;
import de.rainu.lib.jsimpleshell.io.InputConverter;

/**
 * This class contains a little time functionality.
 * 
 * @author rainu
 */
//If you want to convert user input to a custom type, you must implement 
//the InputConverter interface. After them you must add it to shell as handler.
public class Calendar implements InputConverter {

	@Override
	public Object convertInput(String original, Class toClass) throws Exception {
		if(toClass == Date.class){
			return new SimpleDateFormat("yyyy-MM-dd").parseObject(original);
		}
		return null;
	}
	
	@Command(description = "Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this date.")
	public long getTime(
			@Param(name = "date")
			Date date){
		
		return date.getTime();
	}
}
