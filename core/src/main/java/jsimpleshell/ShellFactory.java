package jsimpleshell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.rainu.lib.jsimpleshell.CommandTable;
import de.rainu.lib.jsimpleshell.ConsoleIO;
import de.rainu.lib.jsimpleshell.DashJoinedNamer;
import de.rainu.lib.jsimpleshell.HelpCommandHandler;
import de.rainu.lib.jsimpleshell.util.ArrayHashMultiMap;
import de.rainu.lib.jsimpleshell.util.MultiMap;

public class ShellFactory {

	/**
     * One of facade methods for operating the Shell.
     *
     * Run the obtained Shell with commandLoop().
     *
     * @see de.rainu.lib.jsimpleshell.Shell#Shell(de.rainu.lib.jsimpleshell.Shell.Settings, de.rainu.lib.jsimpleshell.CommandTable, java.util.List)
     *
     * @param prompt Prompt to be displayed
     * @param appName The app name string
     * @param handlers Command handlers
     * @return Shell that can be either further customized or run directly by calling commandLoop().
     */
    public static Shell createShell(String prompt, String appName, Object... handlers) {
        ConsoleIO io = new ConsoleIO();
        JSimpleShellInput input = null;
		try {
			input = new JSimpleShellInput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        List<String> path = new ArrayList<String>(1);
        path.add(prompt);

        MultiMap<String, Object> modifAuxHandlers = new ArrayHashMultiMap<String, Object>();
        modifAuxHandlers.put("!", io);

        Shell theShell = new Shell(new Shell.Settings(input, io, modifAuxHandlers, false),
                new CommandTable(new DashJoinedNamer(true)), path);
        theShell.setAppName(appName);

        theShell.addMainHandler(theShell, "!");
        theShell.addMainHandler(new HelpCommandHandler(), "?");
        for (Object h : handlers) {
            theShell.addMainHandler(h, "");
        }

        input.setCommandTable(theShell.getCommandTable());
        return theShell;
    }
}
