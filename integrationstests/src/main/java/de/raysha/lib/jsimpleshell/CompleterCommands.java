package de.raysha.lib.jsimpleshell;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.completer.CandidatesChooser;

public class CompleterCommands implements CandidatesChooser {
	public static enum TestEnum {
		ARG1, ARG2
	}

	@Command
	public String file(File file){
		return "File: " + file.getPath();
	}

	@Command
	public String VarFile(File...files){
		return "File: " + Arrays.toString(files);
	}

	@Command
	public String dir(
			@Param(value = "file", type = Param.DefaultTypes.DIRECTORY)
			File file){

		return "Dir: " + file.getPath();
	}

	@Command
	public String VarDir(
			@Param(value = "file", type = Param.DefaultTypes.DIRECTORY)
			File...file){

		return "Dir: " + Arrays.toString(file);
	}

	@Command
	public String Boolean(Boolean b){
		return "Boolean: " + b;
	}

	@Command
	public String VarBoolean(Boolean...b){
		return "Boolean: " + Arrays.toString(b);
	}

	@Command
	public String Enum(TestEnum e){
		return "Enum: " + e;
	}

	@Command
	public String VarEnum(TestEnum...e){
		return "Enum: " + Arrays.toString(e);
	}

	@Command
	public String VarLocale(Locale...l){
		return "Locale: " + Arrays.toString(l);
	}

	@Command
	public String custom(
			@Param(value = "p", type = "CompleterCommands")
			String customType){
		return "custom: " + customType;
	}

	@Command
	public String setText(
			@Param(value = "p", type = "Text")
			String text){
		return "text: " + text;
	}

	@Command
	public String VarSpecial(String s, Boolean...b){
		return "Special: " + s + " - " + Arrays.toString(b);
	}

	@Override
	public Candidates chooseCandidates(ShellCommandParamSpec paramSpec, String part) {
		if("CompleterCommands".equals(paramSpec.getType())){
			return new Candidates(Arrays.asList("Candidate1", "Candidate2"));
		}else if("Text".equals(paramSpec.getType())){
			List<String> candidates = new ArrayList<String>(Arrays.asList("ABC Test One", "ABC Test Two", "ABC 2015"));
			Iterator<String> iter = candidates.iterator();
			while(iter.hasNext()){
				if(!iter.next().startsWith(part)){
					iter.remove();
				}
			}

			return new Candidates(candidates);
		}
		return null;
	}
}
