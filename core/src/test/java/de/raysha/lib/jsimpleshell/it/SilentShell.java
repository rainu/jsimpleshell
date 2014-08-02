package de.raysha.lib.jsimpleshell.it;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.ShellBuilder;
import de.raysha.lib.jsimpleshell.ShellCommand;

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
	
	private Thread commandLoop;
	
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
	
	public void start(){
		if(commandLoop == null){
			commandLoop = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						shell.commandLoop();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			commandLoop.setName("Shell-Command-Loop");
		}
		
		commandLoop.start();
	}
	
	public void waitForShell(){
		try {
			commandLoop.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void executeCommand(String cmd, String...arguments) throws IOException {
		StringBuilder sb = new StringBuilder(cmd);
		for(String arg : arguments){
			sb.append(" \"" + arg + "\"");
		}
		sb.append("\n");
		
		simulateUserInput(sb.toString());
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

	public String getErr(){
		return err.toString();
	}
	
	public String getOut(){
		return out.toString();
	}
}
