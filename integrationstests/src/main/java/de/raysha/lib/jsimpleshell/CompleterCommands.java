package de.raysha.lib.jsimpleshell;

import java.io.File;
import java.util.Arrays;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.completer.CandidatesChooser;
import de.raysha.lib.jsimpleshell.completer.FileCandidatesChooser;

public class CompleterCommands implements CandidatesChooser {
	public static enum TestEnum {
		ARG1, ARG2
	}
	
	@Command
	public String file(File file){
		return "File: " + file.getPath();
	}
	
	@Command
	public String dir(
			@Param(value = "file", type = FileCandidatesChooser.DIRECTORY_ONLY_TYPE)
			File file){
		
		return "Dir: " + file.getPath();
	}
	
	@Command
	public String Boolean(Boolean b){
		return "Boolean: " + b;
	}
	
	@Command
	public String Enum(TestEnum e){
		return "Enum: " + e;
	}
	
	@Command
	public String custom(
			@Param(value = "p", type = "CompleterCommands")
			String customType){
		return "custom: " + customType;
	}
	
	@Override
	public Candidates chooseCandidates(ShellCommandParamSpec paramSpec, String part) {
		if("CompleterCommands".equals(paramSpec.getType())){
			return new Candidates(Arrays.asList("Candidate1", "Candidate2"));
		}
		return null;
	}
}
