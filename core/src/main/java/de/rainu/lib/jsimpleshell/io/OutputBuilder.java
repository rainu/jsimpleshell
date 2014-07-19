package de.rainu.lib.jsimpleshell.io;

/**
 * This builder allow to setting up and print a custom output.
 * 
 * @author rainu
 */
public class OutputBuilder {
	private Output out;
	
	public OutputBuilder(Output out) {
		this.out = out;
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
		StringBuffer sb = new StringBuffer();
		
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
			sb.append(obj);
			
			return this;
		}
		
		/**
		 * Add a <b>black-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ black(Object obj){
			sb.append("\u001B[30m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>red-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ red(Object obj){
			sb.append("\u001B[31m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>green-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ green(Object obj){
			sb.append("\u001B[32m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>yellow-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ yellow(Object obj){
			sb.append("\u001B[33m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>blue-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ blue(Object obj){
			sb.append("\u001B[34m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>magenta-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ magenta(Object obj){
			sb.append("\u001B[35m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>cyan-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ cyan(Object obj){
			sb.append("\u001B[36m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>white-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ white(Object obj){
			sb.append("\u001B[37m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>black-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ blackBG(Object obj){
			sb.append("\u001B[40m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>red-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ redBG(Object obj){
			sb.append("\u001B[41m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>green-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ greenBG(Object obj){
			sb.append("\u001B[42m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>yellow-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ yellowBG(Object obj){
			sb.append("\u001B[43m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>blue-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ blueBG(Object obj){
			sb.append("\u001B[44m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>magenta-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ magentaBG(Object obj){
			sb.append("\u001B[45m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>cyan-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ cyanBG(Object obj){
			sb.append("\u001B[46m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Add a <b>white-background-colored</b> text.
		 * 
		 * @param obj
		 * @return This {@link OutputBuilder_}.
		 */
		public OutputBuilder_ whiteBG(Object obj){
			sb.append("\u001B[47m" + obj + "\u001B[0m");
			
			return this;
		}
		
		/**
		 * Print the configured output with appending a new line.
		 */
		public void println() {
			if (isError) {
				out.printlnErr(sb);
			} else {
				out.println(sb);
			}
		}

		/**
		 * Print the configured output.
		 */
		public void print() {
			if (isError) {
				out.printErr(sb);
			} else {
				out.print(sb);
			}
		}
	}
}
