package de.raysha.lib.jsimpleshell.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jline.console.ConsoleReader;

/**
 * This builder allow to read the (user-)input.
 * 
 * @author rainu
 */
public class InputBuilder {
	private final ConsoleReader console;
	private final InputStream in;
	
	public InputBuilder(ConsoleReader console){
		this.console = console;
		this.in = null;
	}
	
	public InputBuilder(InputStream in){
		this.console = null;
		this.in = in;
	}
	
	/**
	 * Get the input with echoing the typed characters.
	 * 
	 * @return A {@link InputBuilder_} instance which can be used get the input.
	 */
	public InputBuilder_ in(){
		return new InputBuilder_(null);	//activate echo
	}
	
	/**
	 * Get the input without echoing any typed characters.
	 * 
	 * @return A {@link InputBuilder_} instance which can be used get the input.
	 */
	public InputBuilder_ invisibleIn(){
		return new InputBuilder_((char)0); //deactivate echo
	}
	
	/**
	 * Get the input where any typed character are replaced by the echoChar.
	 * 
	 * @param echoChar This character will be printed instead of typed character.
	 * @return A {@link InputBuilder_} instance which can be used get the input.
	 */
	public InputBuilder_ maskedIn(char echoChar){
		return new InputBuilder_(echoChar);
	}
	
	public class InputBuilder_ {
		private final Character prevEchoChar;
		private BufferedReader reader;
		private String prompt = "";
		
		private InputBuilder_(Character echoChar) {
			if(console != null){
				this.prevEchoChar = console.getEchoCharacter();
				console.setEchoCharacter(echoChar);
			}else{
				this.prevEchoChar = null;
				this.reader = new BufferedReader(new InputStreamReader(in));
			}
		}
		
		/**
		 * Print a separate prompt before the user input cursor.
		 * 
		 * @param prompt The prompt
		 * @return This {@link InputBuilder_}
		 */
		public InputBuilder_ withPromt(String prompt){
			this.prompt = prompt;
			
			return this;
		}
		
		/**
		 * Read a line from the (user-)input, and return the line (without any trailing newlines).
		 * 
		 * @return The input as string.
		 * @throws IOException
		 */
		public String readLine() throws IOException {
			if(console != null){
				try{
					return console.readLine(prompt, console.getEchoCharacter());
				}finally{
					console.setEchoCharacter(prevEchoChar);
				}
			}else{
				return reader.readLine();
			}
		}
	}
}
