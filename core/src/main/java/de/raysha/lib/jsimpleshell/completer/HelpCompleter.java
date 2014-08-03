package de.raysha.lib.jsimpleshell.completer;

import java.util.Collection;
import java.util.List;

import jline.console.completer.FileNameCompleter;
import jline.console.completer.StringsCompleter;

/**
 * This {@link FileNameCompleter} is only active when the cursor is at an argument position after the ?help command.
 * 
 * @author rainu
 */
public class HelpCompleter extends StringsCompleter {

	public HelpCompleter() {
		super();
	}

	public HelpCompleter(Collection<String> strings) {
		super(strings);
	}

	public HelpCompleter(String... strings) {
		super(strings);
	}
	

	@Override
	public int complete(String buffer, int cursor, List<CharSequence> candidates) {
		if(cursor > 0){
			String start = buffer.substring(0, cursor).replace("\t", "");
			
			if(start.matches("^\\s*\\?help\\s.*")){
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