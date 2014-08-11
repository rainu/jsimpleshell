package de.raysha.lib.jsimpleshell;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.handler.InputConverter;
import de.raysha.lib.jsimpleshell.handler.OutputConverter;

public class ConverterCommands implements InputConverter, OutputConverter {

	public static class Entry {
		private String key;
		private String value;
		
		public Entry(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return key + " => " + value;
		}
	}
	
	@Override
	public Object convertOutput(Object toBeFormatted) {
		if(toBeFormatted instanceof Entry) {
			return ((Entry) toBeFormatted).getKey() + ";" + ((Entry) toBeFormatted).getValue();
		}
		
		return null;
	}

	@Override
	public Object convertInput(String original, Class toClass) throws Exception {
		if(toClass == Entry.class){
			String[] split = original.split("\\;");
			
			return new Entry(split[0], split[1]);
		}
		
		return null;
	}

	@Command
	public Entry toEntry(String key, String value){
		return new Entry(key, value);
	}
	
	@Command
	public String fromEntry(Entry entry){
		return entry.toString();
	}
}
