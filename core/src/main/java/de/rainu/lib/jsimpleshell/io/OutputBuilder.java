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
