package de.raysha.lib.jsimpleshell.script.cmd.process;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.raysha.lib.jsimpleshell.Shell;

/**
 * This class is responsible for control a {@link Process}.
 * @author rainu
 *
 */
public class ProcessController {
	private final Process process;
	private final Shell shell;
	private boolean printErr;
	private boolean printOut;

	public ProcessController(Process process, Shell shell) {
		this.process = process;
		this.shell = shell;
	}

	public void setPrintErr(boolean printErr) {
		this.printErr = printErr;
	}

	public void setPrintOut(boolean printOut) {
		this.printOut = printOut;
	}


	/**
	 * Wait for my {@link Process}. This method is blocking, which means that
	 * after this method my {@link Process} is finished. Input/Error/Output will be piped to my
	 * {@link Shell}s Input/Error/Output and the Output/Error will also be
	 * stored into a temporary file. After the process is finished, a {@link ProcessResult}
	 * will be returned.
	 *
	 * @return The {@link ProcessResult} that contains all informations about my {@link Process}.
	 * @throws IOException If the temporary files could not be created.
	 */
	public ProcessResult waitForProcess() throws IOException {
		final ProcessResult result = new ProcessResult();

		result.setOutput(File.createTempFile("jss-process", ".out"));
		result.setError(File.createTempFile("jss-process", ".err"));
		result.getOutput().deleteOnExit();
		result.getError().deleteOnExit();

		final FileOutputStream outStream = new FileOutputStream(result.getOutput());
		final FileOutputStream errStream = new FileOutputStream(result.getError());

		InputStreamPipe outPipe = new InputStreamPipe(process.getInputStream(), outStream);
		if(printOut) outPipe.addOutputStream(new ShellOutStream());

		InputStreamPipe errPipe = new InputStreamPipe(process.getErrorStream(), errStream);
		if(printErr) errPipe.addOutputStream(new ShellErrStream());

		OutputStreamPipe inPipe = new OutputStreamPipe(process.getOutputStream(),
				new ShellInStream());

		outPipe.setName("OUTPUT-PIPE"); outPipe.start();
		errPipe.setName("ERROR-PIPE"); errPipe.start();
		inPipe.setName("INPUT-PIPE"); inPipe.start();

		try {
			result.setRC(process.waitFor());
		} catch (InterruptedException e) {
			result.setInterruptedException(e);
		}

		stopThread(outPipe);
		stopThread(errPipe);
		stopThread(inPipe);

		closeStream(outStream);
		closeStream(errStream);

		return result;
	}

	private void stopThread(Thread thread) {
		try{
			thread.interrupt();
			thread.join(500);
			if(thread.isAlive()){
				thread.stop(); //die hard!
			}
		}catch(Exception e){
			//do nothing
		}
	}

	private void closeStream(Closeable cloasable) {
		try{
			cloasable.close();
		}catch(Exception e){
			//do nothing
		}
	}

	private class InputStreamPipe extends Thread {
		private final InputStream input;
		private final List<OutputStream> outputs;

		public InputStreamPipe(InputStream in, OutputStream...out) {
			this.input = in;
			this.outputs = new ArrayList<OutputStream>(Arrays.asList(out));
		}

		public void addOutputStream(OutputStream out) {
			outputs.add(out);
		}

		public void run(){
			try {
				byte[] buffer = new byte[8192];

				while(!interrupted()){
					int read = input.read(buffer);
					if(read < 0) break;

					for(OutputStream out : outputs){
						out.write(buffer, 0, read);
						out.flush();
					}
				}
	        } catch (IOException ioe) { }
		}
	}

	private class OutputStreamPipe extends Thread {
		private final OutputStream output;
		private final InputStream input;

		public OutputStreamPipe(OutputStream out, InputStream in) {
			this.input = in;
			this.output = out;
		}

		public void run(){
			try {
				byte[] buffer = new byte[8192];

				while(!interrupted()){
					int read = input.read(buffer);
					if(read < 0) break;

					output.write(buffer, 0, read);
					output.flush();
				}
	        } catch (IOException ioe) { }
		}
	}

	private class ShellOutStream extends OutputStream {
		@Override
		public void write(int b) throws IOException {
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			shell.getOutputBuilder().out()
				.normal(new String(b, off, len))
			.print();
		}
	}

	private class ShellErrStream extends OutputStream {
		@Override
		public void write(int b) throws IOException {
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			shell.getOutputBuilder().err()
				.normal(new String(b, off, len))
			.print();
		}
	}

	private class ShellInStream extends InputStream {
		private byte[] remaining = new byte[]{};

		@Override
		public int read() throws IOException {
			return 0;
		}

		@Override
		public int read(byte[] b) throws IOException {
			if(remaining.length == 0){
				String read = shell.getInputBuilder().in().readLine() + "\n";

				remaining = read.getBytes();
			}

			for(int i=0; i < remaining.length; i++){
				if(b.length == i){	//b is full!
					byte[] newRemaining = new byte[remaining.length - i];
					for(int j=i; j < remaining.length; j++){
						newRemaining[j-i] = remaining[j];
					}
					remaining = newRemaining;
					return b.length;
				}

				b[i] = remaining[i];
			}

			int len = remaining.length;
			remaining = new byte[]{};

			return len;
		}
	}
}
