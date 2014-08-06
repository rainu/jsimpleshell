package de.raysha.lib.jsimpleshell;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

public class SilentShell {
	final Shell shell;
	
	private PipedOutputStream in;
	private ByteArrayOutputStream out;
	private ByteArrayOutputStream err;
	
	private final Thread commandLoop;
	private final Pattern promtLinePattern = Pattern.compile("^IT.*\\>.*$");
	
	public SilentShell(ShellBuilder builder) throws IOException {
		this.shell = setup(builder);
		this.commandLoop = new CommandLoopThread(shell);
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
		commandLoop.start();
	}
	
	public void stop() {
		commandLoop.interrupt();
	}
	
	public CommandResult waitForShellCommandExec(){
		StringBuilder err = new StringBuilder();
		StringBuilder out = new StringBuilder();
		
		while(commandLoop.isAlive()){
			err.append(getErr());
			out.append(getOut());
			
			String lastLine = null;
			
			int lastIndex = out.lastIndexOf("\n");
			if(lastIndex < 0 && out.toString().startsWith("IT")){
				lastLine = out.toString();
			}else if(lastIndex >= 0){
				lastLine = out.substring(lastIndex + 1).toString();
			}
			
			if(lastLine != null && promtLinePattern.matcher(lastLine).matches()){
				break;
			}
			
			try {
				Thread.sleep(150);	//TODO: Sleep is a stupid solution :(
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		
		return new CommandResult(out.toString(), err.toString());
	}
	
	public void waitForShellExit(){
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
	
	public void simulateUserInput(String userInput) throws IOException{
		in.write(userInput.getBytes());
		in.flush();
	}

	private String getErr(){
		try{
			return err.toString();
		}finally{
			this.err.reset();
		}
	}
	
	private String getOut(){
		try{
			return out.toString();
		}finally{
			this.out.reset();
		}
	}
}
