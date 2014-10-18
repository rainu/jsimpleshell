package de.raysha.lib.jsimpleshell.script.cmd;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EchoCommandHandlerTest {

	final EchoCommandHandler toTest = new EchoCommandHandler();

	@Test
	public void applyReplacements(){
		assertEquals(" \u0007", applyReplacement(" \\a"));
		assertEquals(" \b", applyReplacement(" \\b"));
		assertEquals(" \u001B", applyReplacement(" \\e"));
		assertEquals(" \f", applyReplacement(" \\f"));
		assertEquals(" \n", applyReplacement(" \\n"));
		assertEquals(" \r", applyReplacement(" \\r"));
		assertEquals(" \t", applyReplacement(" \\t"));
		assertEquals(" \u000B", applyReplacement(" \\v"));
		assertEquals(" S", applyReplacement(" \\0123"));
		assertEquals(" S", applyReplacement(" \\x0053"));

		assertEquals("\u0007", applyReplacement("\\a"));
		assertEquals("\b", applyReplacement("\\b"));
		assertEquals("\u001B", applyReplacement("\\e"));
		assertEquals("\f", applyReplacement("\\f"));
		assertEquals("\n", applyReplacement("\\n"));
		assertEquals("\r", applyReplacement("\\r"));
		assertEquals("\t", applyReplacement("\\t"));
		assertEquals("\u000B", applyReplacement("\\v"));
		assertEquals("S", applyReplacement("\\0123"));
		assertEquals("S", applyReplacement("\\x0053"));

		assertEquals("\\a", applyReplacement("\\\\a"));
		assertEquals("\\b", applyReplacement("\\\\b"));
		assertEquals("\\e", applyReplacement("\\\\e"));
		assertEquals("\\f", applyReplacement("\\\\f"));
		assertEquals("\\n", applyReplacement("\\\\n"));
		assertEquals("\\r", applyReplacement("\\\\r"));
		assertEquals("\\t", applyReplacement("\\\\t"));
		assertEquals("\\v", applyReplacement("\\\\v"));
		assertEquals("\\0123", applyReplacement("\\\\0123"));
		assertEquals("\\x0053", applyReplacement("\\\\x0053"));

		assertEquals("hello", applyReplacement("hello\\cworld"));
		assertEquals("hello\\world", applyReplacement("hello\\\\world"));
	}

	private String applyReplacement(String string){
		return toTest.applyReplacements(new StringBuilder(string));
	}
}
