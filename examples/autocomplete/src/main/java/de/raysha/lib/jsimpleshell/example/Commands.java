package de.raysha.lib.jsimpleshell.example;

import java.io.File;
import java.util.Locale;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.completer.CommandNameCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.FileCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.LocaleCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.MacroNameCandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.VariableCandidatesChooser;

public class Commands {

	@Command
	public String binary(
			@Param(type = BinaryCandidatesChooser.BINARY_TYPE)
			String binary){
		return "Binary: " + binary;
	}

	@Command
	public String file(
			//file auto complete does only works if the mechanism is active (by default)
			//you can change it on building a shell (ShellBuilder->Behavior)
			File file){
		return file2(file.getPath());
	}

	@Command
	public String file2(
			//the type parameter of the @Param-annotation can be used by the custom CandidatesChooser
			@Param(type = FileCandidatesChooser.FILES_TYPE)
			String filePath){
		return "File: " + filePath;
	}

	@Command
	public String dir(
			//the File argument will be basically interpret as file
			//but if you customize the type via the type-parameter you can
			//configure that the FilesCandidatesChooser will only auto complete
			//directories
			@Param(type = FileCandidatesChooser.DIRECTORY_ONLY_TYPE)
			File dir){
		return dir2(dir.getPath());
	}

	@Command
	public String dir2(
			@Param(type = FileCandidatesChooser.DIRECTORY_ONLY_TYPE)
			String dirPath){
		return "Directory: " + dirPath;
	}

	public static enum ColorName {
		RED, BLUE, GREEN
	}

	@Command
	public String color(
			//enums can be auto complete out of the box
			ColorName color){
		return "Color: " + color;
	}

	@Command
	public String command(
			@Param(type = CommandNameCandidatesChooser.COMMAND_NAME_TYPE)
			String cmd){
		return "Command: " + cmd;
	}

	@Command
	public String locale(Locale l){
		return "Locale: " + l;
	}

	@Command
	public String locale2(
			@Param(type = LocaleCandidatesChooser.LOCALE_TYPE)
			String l){
		return "Locale: " + l;
	}

	@Command
	public String macro(
			@Param(type = MacroNameCandidatesChooser.MACRO_NAME_TYPE)
			String macro){

		return "Macro: " + macro;
	}

	@Command
	public String variable(
			@Param(type = VariableCandidatesChooser.VARIABLE_NAME_TYPE)
			String var){

		return "Variable: " + var;
	}
}
