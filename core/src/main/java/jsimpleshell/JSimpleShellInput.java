package jsimpleshell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;
import asg.cliche.CommandTable;
import asg.cliche.Input;
import asg.cliche.ShellCommand;

public class JSimpleShellInput implements Input {
	private final ConsoleReader reader;
	
	public JSimpleShellInput() throws IOException {
		reader = new ConsoleReader();
		reader.setPrompt(">");
	}
	
	public void setCommandTable(CommandTable commandTable) {
		reader.addCompleter(new StringsCompleter(getNames(commandTable)));
	}
	
	private Collection<String> getNames(CommandTable commandTable) {
		Collection<String> names = new ArrayList<String>();
		
		for(ShellCommand cmd : commandTable.getCommandTable()){
			if("".equals(cmd.getPrefix())){
				names.add(cmd.getName());
			}
		}
		
		return names;
	}

	@Override
	public String readCommand(List<String> path) {
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
