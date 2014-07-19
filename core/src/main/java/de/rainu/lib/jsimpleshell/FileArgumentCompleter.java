package de.rainu.lib.jsimpleshell;

import java.util.List;

import jline.console.completer.FileNameCompleter;

/**
 * This {@link FileNameCompleter} is only active when the cursor is at an argument position.
 * 
 * @author rainu
 */
public class FileArgumentCompleter extends FileNameCompleter {

	@Override
	public int complete(String buffer, int cursor, List<CharSequence> candidates) {
		if(cursor > 0){
			String start = buffer.substring(0, cursor).replace("\t", "");
			if(start.contains(" ") && !start.matches("^\\s*$")){
				int startIndex = cursor;
				for(int i = cursor - 1; i >= 0; i--){
					if(buffer.charAt(i) == ' '){
						startIndex = i;
						break;
					}
				}
				String part = buffer.substring(startIndex + 1);

				return super.complete(part, cursor - startIndex - 2, candidates) + start.length() - part.length();
			}
		}
	
		//the cursor is not on a argument position!
		return -1;
	}
}
