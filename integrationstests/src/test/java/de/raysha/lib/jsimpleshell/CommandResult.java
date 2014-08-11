package de.raysha.lib.jsimpleshell;


public class CommandResult{
	private String out;
	private String err;
	
	CommandResult(String out, String err){
		this.out = out;
		this.err = err;
	}
	
	public String getOut(){
		return out;
	}
	public String getErr(){
		return err;
	}
	
	@Override
	public String toString() {
		return "[OUT]\n" + getOut() + "\n\n[ERROR]\n" + getErr();
	}

	public boolean isError() {
		return !getErr().isEmpty();
	}
	
	public boolean outContains(String string) {
		return getOut().contains(string);
	}

	public boolean containsOutLine(String string) {
		for(String line : getOutLines()){
			if(line.matches(string)) return true;
		}
		return false;
	}
	
	public boolean containsErrLine(String string) {
		for(String line : getErrLines()){
			if(line.matches(string)) return true;
		}
		return false;
	}

	private String[] outLines;
	
	private String[] getOutLines() {
		if(outLines == null){
			outLines = getOut().replace("\r", "\n").split("\\\n");
		}
		
		return outLines;
	}
	
	private String[] errLines;
	
	private String[] getErrLines() {
		if(errLines == null){
			errLines = getErr().replace("\r", "\n").split("\\\n");
		}
		
		return errLines;
	}
}