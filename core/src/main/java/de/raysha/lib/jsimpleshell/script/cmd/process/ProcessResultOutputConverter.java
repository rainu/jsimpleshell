package de.raysha.lib.jsimpleshell.script.cmd.process;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import de.raysha.lib.jsimpleshell.handler.OutputConverter;

/**
 * This {@link OutputConverter} is responsible for converting a {@link ProcessResult}.
 *
 * @author rainu
 */
public class ProcessResultOutputConverter implements OutputConverter {

	@Override
	public Object convertOutput(Object toBeFormatted) {
		if(! (toBeFormatted instanceof ProcessResult)) return null;

		final ProcessResult result = (ProcessResult)toBeFormatted;

		if(result.getOutput().length() == 0) return "";

		StringBuilder content = new StringBuilder((int)result.getOutput().length());

		try{
			Reader reader = new BufferedReader(
								new InputStreamReader(
										new FileInputStream(result.getOutput())));
		    try {
		        char[] buffer = new char[8192];
		        int read;
		        while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
		        	content.append(buffer, 0, read);
		        }
		    } finally {
		        try{ reader.close(); }catch(Exception e){}
		    }
		}catch(IOException e){
			content.append("Failed to read the process output: ");
			content.append(e.getMessage());
		}

		return content.toString();
	}

}
