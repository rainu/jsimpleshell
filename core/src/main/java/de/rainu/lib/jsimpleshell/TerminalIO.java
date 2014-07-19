package de.rainu.lib.jsimpleshell;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import jline.console.ConsoleReader;
import de.rainu.lib.jsimpleshell.util.Strings;

/**
 * This class uses a {@link ConsoleReader} for additional functionality.
 * 
 * @author rainu
 *
 */
public class TerminalIO implements Input, Output {
	private ConsoleReader console;
	private PrintStream error;

	private int lastCommandOffset = 0;

	public TerminalIO(ConsoleReader console) {
		this(console, null);
	}

	public TerminalIO(ConsoleReader console, OutputStream error) {
		this.console = console;
		this.error = error != null ? new PrintStream(error) : null;
	}

	@Override
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
			for (Object elem : (Collection) obj) {
				output(elem, 0, oce);
			}
		} else {
			output(obj, 0, oce);
		}
	}

	@Override
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

	@Override
	public void outputException(Throwable e) {
		String stackTrace = extractStacktrace(e);
		printlnErr(stackTrace);
	}

	@Override
	public void outputHeader(String text) {
		if (text != null) {
			println(text);
		}
	}

	@Override
	public String readCommand(List<String> path) {
		try {
			String prompt = Strings.joinStrings(path, false, '/');
			lastCommandOffset = prompt.length();

			return console.readLine(prompt);
		} catch (IOException e) {
			throw new Error(e);
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
			for (Object elem : (Collection) obj) {
				output(elem, indent + 1, oce);
			}
		} else if (obj instanceof Throwable) {
			outputException((Throwable)obj);
		} else {
			println(obj);
		}
	}

	private String extractStacktrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);

		return sw.toString(); // stack trace as a string
	}

	private void printlnErr(Object o) {
		if (error == null) {
			println("[ERROR] " + o);
		} else {
			error.println(o);
		}
	}

	private void printErr(Object o) {
		if (error == null) {
			print("[ERROR] " + o);
		} else {
			error.print(o);
		}
	}

	private void println(Object o) {
		try {
			console.println(String.valueOf(o));
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	private void print(Object o) {
		try {
			console.print(String.valueOf(o));
		} catch (IOException e) {
			throw new Error(e);
		}
	}
}
