package de.raysha.lib.jsimpleshell.script.cmd.process;

import java.io.File;

/**
 * This class contains all informations about a finished {@link Process}.
 *
 * @author rainu
 */
public class ProcessResult {
	private File out;
	private File err;
	private int rc;
	private InterruptedException interruptedException;

	void setOutput(File outputFile) {
		this.out = outputFile;
	}

	void setError(File errorFile) {
		this.err = errorFile;
	}

	void setRC(int rc) {
		this.rc = rc;
	}

	void setInterruptedException(InterruptedException e) {
		this.interruptedException = e;
	}

	/**
	 * Return the {@link File} in which the output of the process is stored.
	 * This file will be delete after the VM is shutting down.
	 *
	 * @return The {@link File}.
	 */
	public File getOutput() {
		return out;
	}

	/**
	 * Return the {@link File} in which the error of the process is stored.
	 * This file will be delete after the VM is shutting down.
	 *
	 * @return The {@link File}.
	 */
	public File getError() {
		return err;
	}

	/**
	 * Returns the exit value of the process. By convention, the value 0 indicates normal termination.
	 *
	 * @return The exit value.
	 */
	public int getRc() {
		return rc;
	}

	/**
	 * Get the thrown {@link InterruptedException} if there was a one thrown.
	 *
	 * @return The thrown {@link InterruptedException} if the process was interrupted. Otherwise null.
	 */
	public InterruptedException getInterruptedException() {
		return interruptedException;
	}

	/**
	 * Was the process successful?
	 *
	 * @return True if the return code is 0. Returns false if a {@link InterruptedException} was thrown or
	 * the return code is not 0.
	 */
	public boolean wasSuccessful(){
		if(interruptedException != null){
			return false;
		}

		if(rc == 0) return true;

		return false;
	}
}
