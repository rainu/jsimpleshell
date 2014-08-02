package de.raysha.lib.jsimpleshell.io;

import de.raysha.lib.jsimpleshell.util.ColoredStringBuilder;

/**
 * This builder allow to setting up and print a custom output.
 * 
 * @author rainu
 */
public class OutputBuilder {
	private Output out;
	private boolean colorOut = true;
	
	public OutputBuilder(Output out) {
		this.out = out;
	}
	
	/**
	 * Disable color output. No colored text will be printed even if the
	 * color-output-methods was called.
	 */
	public void disableColor() {
		this.colorOut = false;
	}
	
	/**
	 * Enable color output.
	 */
	public void enableColor() {
		this.colorOut = true;
	}

	/**
	 * Prepare a to print to out.
	 * 
	 * @return A {@link OutputBuilder_} instance which can be used to print out to output-stream.
	 */
	public OutputBuilder_ out(){
		return new OutputBuilder_(false);
	}
	
	/**
	 * Prepare a to print to err.
	 * 
	 * @return A {@link OutputBuilder_} instance which can be used to print out to error-stream.
	 */
	public OutputBuilder_ err(){
		return new OutputBuilder_(true);
	}
	
	public class OutputBuilder_ {
		final boolean isError;
		ColoredStringBuilder csb = new ColoredStringBuilder();
		
		private OutputBuilder_(boolean err) {
			this.isError = err;
		}
		
		/**
		 * Add a text without color.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ normal(Object obj){
			csb.normal(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>black-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ black(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.black(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>red-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ red(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.red(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>green-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ green(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.green(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>yellow-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ yellow(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.yellow(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>blue-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ blue(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.blue(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>magenta-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ magenta(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.magenta(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>cyan-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ cyan(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.cyan(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>white-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ white(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.white(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>black-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ blackBG(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.blackBG(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>red-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ redBG(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.redBG(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>green-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ greenBG(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.greenBG(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>yellow-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ yellowBG(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.yellowBG(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>blue-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ blueBG(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.blueBG(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>magenta-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ magentaBG(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.magentaBG(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>cyan-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ cyanBG(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.cyanBG(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>white-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ whiteBG(Object obj){
			if(!colorOut) return normal(obj);
			
			csb.whiteBG(obj);
			
			return this;
		}
		
		/**
		 * Print the configured output with appending a new line.
		 */
		public void println() {
			if (isError) {
				out.printlnErr(csb.build());
			} else {
				out.println(csb.build());
			}
		}

		/**
		 * Print the configured output.
		 */
		public void print() {
			if (isError) {
				out.printErr(csb.build());
			} else {
				out.print(csb.build());
			}
		}
	}
}
