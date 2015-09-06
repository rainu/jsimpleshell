package de.raysha.lib.jsimpleshell.example;

import de.raysha.lib.jsimpleshell.CommandRecorder;
import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.ExitException;

import java.io.IOException;

public class Commands {

	@Inject
	private Shell parent;	//the parent shell is needed for the subshell

	@Command(startsSubshell = true)	//it is VERY important that you mark this command as "start a subshell"
	public String build() throws IOException {
		NameBuilder builder = new NameBuilder();

		//build the subshell
		Shell subshell = ShellBuilder.subshell("build-your-name", parent)
			.behavior()
				.disableExitCommand("cancel")	//disable the default "exit" command to implements the commit/cancel feature
				.addHandler(builder)
			.build();

		subshell.commandLoop();		//enter the subshell

		if(CommandRecorder.isShellInRecordMode(parent)){
			//skip processing if the shell is in record mode
			//for example if the user configure a loop or a condition (if)
			return null;
		}

		//evaluate the result
		if(builder.isCanceled()){
			return "You have canceled the process!";
		}

		return "Name: " + builder.getName();
	}

	public static class NameBuilder {
		private boolean isCanceled = true;

		private String firstName = "";
		private String middleName = "";
		private String lastName = "";

		@Command
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		@Command
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		@Command
		public void setMiddleName(String middleName) {
			this.middleName = middleName;
		}

		@Command
		public void cancel() throws ExitException{
			throw new ExitException("Process was canceled!");
		}

		@Command
		public void commit() throws ExitException{
			this.isCanceled = false;
			throw new ExitException();	//if an command throws this exception the currently shell will be exit
		}

		public String getName(){
			return firstName  + (middleName != null ? " " : "") + middleName + " " + lastName;
		}

		public boolean isCanceled() {
			return isCanceled;
		}
	}
}
