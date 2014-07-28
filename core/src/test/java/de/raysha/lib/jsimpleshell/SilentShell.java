package de.raysha.lib.jsimpleshell;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import org.apache.commons.io.FileUtils;

import de.raysha.lib.jsimpleshell.exception.CLIException;

public class SilentShell {
	public static class CommandResult{
		private String out;
		private String err;
		
		private CommandResult(String out, String err){
			this.out = out;
			this.err = err;
		}
		
		public String getOut(){
			return out;
		}
		public String getErr(){
			return err;
		}
	}
	
	final Shell shell;
	
	private PipedOutputStream in;
	private ByteArrayOutputStream out;
	private ByteArrayOutputStream err;
	
	public SilentShell(ShellBuilder builder) throws IOException {
		this.shell = setup(builder);
	}

	private Shell setup(ShellBuilder builder) throws IOException {
		out = new ByteArrayOutputStream();
	    PrintStream psOut = new PrintStream(out);
	    
	    err = new ByteArrayOutputStream();
	    PrintStream psErr = new PrintStream(err);

	    in = new PipedOutputStream();
	    
	    PipedInputStream worldIn = new PipedInputStream();
	    worldIn.connect(in);
	    
	    builder.setConsole(worldIn, psOut);
	    builder.setError(psErr);
	    
		return builder.build();
	}
	

	public CommandResult executeCommand(String cmd, String...arguments) throws IOException, CLIException {
		StringBuilder sb = new StringBuilder(cmd);
		for(String arg : arguments){
			sb.append(" \"" + arg + "\"");
		}
		sb.append("\n");
		
		final PrintStream origOut = System.out;
		final PrintStream origErr = System.err;

		File tmpOut = null;
		File tmpErr = null;
		
		try{
			try{
				tmpOut = File.createTempFile("jsimpleshell", ".out");
				tmpOut.deleteOnExit();
				tmpErr = File.createTempFile("jsimpleshell", ".err");
				tmpErr.deleteOnExit();
				System.setOut(new PrintStream(tmpOut));
				System.setErr(new PrintStream(tmpErr));
			}catch(IOException e){
				e.printStackTrace();
			}
			
			simulateUserInput(sb.toString());
		}finally{
			System.setOut(origOut);
			System.setErr(origErr);
		}
		
		return readResult(tmpOut, tmpErr);
	}
	
	public String getLongCommandName(String cmd){
		for(ShellCommand shCmd : shell.getCommandTable().getCommandTable()){
			if(cmd.equals(shCmd.getAbbreviation()) || cmd.equals(shCmd.getName())){
				return shCmd.getName();
			}
		}
		
		return null;
	}
	
	public String getShortCommandName(String cmd){
		for(ShellCommand shCmd : shell.getCommandTable().getCommandTable()){
			if(cmd.equals(shCmd.getAbbreviation()) || cmd.equals(shCmd.getName())){
				return shCmd.getAbbreviation();
			}
		}
		
		return null;
	}
	
	public String getCommandMethodName(String cmd){
		for(ShellCommand shCmd : shell.getCommandTable().getCommandTable()){
			if(cmd.equals(shCmd.getAbbreviation()) || cmd.equals(shCmd.getName())){
				return shCmd.getMethod().getName();
			}
		}
		
		return null;
	}
	
	public void simulateUserInput(String userInput) throws IOException{
		in.write(userInput.getBytes());
		in.flush();
	}

	private CommandResult readResult(File fOut, File fErr) {
		CommandResult result = new CommandResult(
				out.toString(), 
				err.toString());
		
		try{
			result.out = result.out + FileUtils.readFileToString(fOut);
		}catch(IOException e){
			e.printStackTrace();
		}
		try{
			result.err = result.err + FileUtils.readFileToString(fErr);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		out.reset();
		err.reset();
		
		return result;
	}
}
