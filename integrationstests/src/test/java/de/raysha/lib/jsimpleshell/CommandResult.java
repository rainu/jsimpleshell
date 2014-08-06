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

	public boolean containsLine(String string) {
		for(String line : getLines()){
			if(line.matches(string)) return true;
		}
		return false;
	}

	private String[] lines;
	
	private String[] getLines() {
		if(lines == null){
			lines = getOut().replace("\r", "\n").split("\\\n");
		}
		
		return lines;
	}
}