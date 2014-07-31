/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package de.raysha.lib.jsimpleshell.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.exception.TokenException;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.handler.ShellManageable;
import de.raysha.lib.jsimpleshell.util.Strings;

/**
 * Console IO subsystem.
 * This is also one of special command handlers and is responsible
 * for logging (duplicating output) and execution of scripts.
 *
 * @author ASG
 */
public class ConsoleIO implements Input, Output, ShellManageable {
	private MessageResolver messageResolver;
	
    public ConsoleIO(BufferedReader in, PrintStream out, PrintStream err) {
        this.in = in;
        this.out = out;
        this.err = err;
    }

    public ConsoleIO() {
        this(new BufferedReader(new InputStreamReader(System.in)),
                System.out, System.err);
    }

    private BufferedReader in;
    private PrintStream out;
    private PrintStream err;

    private int lastCommandOffset = 0;
    
    @Override
    public void setMessageResolver(MessageResolver messageResolver) {
    	this.messageResolver = messageResolver;
    }

    public String readCommand(List<String> path) {
        try {
            String prompt = Strings.joinStrings(path, false, '/');
            switch (inputState) {
                case USER: 
                    return readUsersCommand(prompt);
                case SCRIPT:
                    String command = readCommandFromScript(prompt);
                    if (command != null) {
                        return command;
                    } else {
                        closeScript();
                        return readUsersCommand(prompt);
                    }
            }
            return readUsersCommand(prompt);
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    private static final String USER_PROMPT_SUFFIX = "> ";
    private static final String FILE_PROMPT_SUFFIX = "$ ";

    private static enum InputState { USER, SCRIPT }

    private InputState inputState = InputState.USER;

    private String readUsersCommand(String prompt) throws IOException {
        String completePrompt = prompt+ USER_PROMPT_SUFFIX;
        print(completePrompt);
        lastCommandOffset = completePrompt.length();
        
        String command = in.readLine();
        if (log != null) {
            log.println(command);
        }
        return command;
    }

    private BufferedReader scriptReader = null;

    private String readCommandFromScript(String prompt) throws IOException {
        String command = scriptReader.readLine();
        if (command != null) {
            String completePrompt = prompt+ FILE_PROMPT_SUFFIX;
            print(completePrompt);
            lastCommandOffset = completePrompt.length();
        }
        return command;
    }

    private void closeScript() throws IOException {
        if (scriptReader != null) {
            scriptReader.close();
            scriptReader = null;
        }
        inputState = InputState.USER;
    }

    @Command(abbrev = "command.abbrev.runscript", description = "command.description.runscript", 
    		header = "command.header.runscript", name = "command.name.runscript")
    public void runScript(
            @Param(name="param.name.runscript", description="param.description.runscript") 
                String filename
            ) throws FileNotFoundException {

        scriptReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        inputState = InputState.SCRIPT;
    }


    public void outputHeader(String text) {
        if (text != null) {
            println(text);
        }
    }

    public void output(Object obj, OutputConversionEngine oce) {
        if (obj == null) {
            return;
        } else {
            obj = oce.convertOutput(obj);
        }

        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                output(Array.get(obj, i), 0, oce);
            }
        } else if (obj instanceof Collection) {
            for (Object elem : (Collection)obj) {
                output(elem, 0, oce);
            }
        } else {
            output(obj, 0, oce);
        }
    }

    private void output(Object obj, int indent, OutputConversionEngine oce) {
        if (obj == null) {
            return;
        }

        if (obj != null) {
            obj = oce.convertOutput(obj);
        }

        for (int i = 0; i < indent; i++) {
            print("\t");
        }

        if (obj == null) {
            println("(null)");
        } else if (obj.getClass().isPrimitive() || obj instanceof String) {
            println(obj);
        } else if (obj.getClass().isArray()) {
            println("Array");
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                output(Array.get(obj, i), indent + 1, oce);
            }
        } else if (obj instanceof Collection) {
            println("Collection");
            for (Object elem : (Collection)obj) {
                output(elem, indent + 1, oce);
            }
        } else if (obj instanceof Throwable) {
            println(obj); // class and its message
            ((Throwable)obj).printStackTrace(out);
        } else {
            println(obj);
        }
    }

    public void print(Object x) {
    	x = resolve(x);
    	
        out.print(x);
        if (log != null) {
            log.print(x);
        }
    }

    public void println(Object x) {
    	x = resolve(x);
    	
        out.println(x);
        if (log != null) {
            log.println(x);
        }
    }

    public void printErr(Object x) {
    	x = resolve(x);
    	
        err.print(x);
        if (log != null) {
            log.print(x);
        }
    }

    public void printlnErr(Object x) {
    	x = resolve(x);
    	
        err.println(x);
        if (log != null) {
            log.println(x);
        }
    }
    
    private Object resolve(Object x){
    	if(x instanceof String){
    		return messageResolver.resolveGeneralMessage((String)x);
    	}
    	
    	return x;
    }

    public void outputException(String input, TokenException error) {
        int errIndex = error.getToken().getIndex() + lastCommandOffset;
        while (errIndex-- > 0) {
            printErr("-");
        }
        for (int i = 0; i < error.getToken().getString().length(); i++) {
            printErr("^");
        }
        printlnErr("");
        printlnErr(error);
    }

    public void outputException(Throwable e) {
        printlnErr(e);
        if (e.getCause() != null) {
            printlnErr(e.getCause());
        }
    }
    
    private PrintStream log = null;

    private boolean isLoggingEnabled() {
        return log != null;
    }

    private int loopCounter = 0;

    public void cliEnterLoop(Shell shell) {
        if (isLoggingEnabled()) {
            loopCounter++;
        }
    }

    public void cliLeaveLoop(Shell shell) {
        if (isLoggingEnabled()) {
            loopCounter--;
        }
        if (loopCounter < 0) {
            disableLogging();
        }
    }

    @Command(abbrev = "command.abbrev.enablelogging", description = "command.description.enablelogging", 
    		header = "command.header.enablelogging", name = "command.name.enablelogging")
    public void l(
            @Param(name="param.name.enablelogging", description="param.description.enablelogging") String filename
            ) throws FileNotFoundException {
        
        log = new PrintStream(filename);
        loopCounter = 0;
    }

    @Command(abbrev = "command.abbrev.disablelogging", description = "command.description.disablelogging", 
    		header = "command.header.disablelogging", name = "command.name.disablelogging")
    public String disableLogging() {
        if (log != null) {
            log.close();
            log = null;
            return "Logging disabled";
        } else return "Logging is already disabled";
    }

}
