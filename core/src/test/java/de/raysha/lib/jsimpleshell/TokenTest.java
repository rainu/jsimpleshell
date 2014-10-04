/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package de.raysha.lib.jsimpleshell;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

/**
 *
 * @author ASG
 */
public class TokenTest {

	/**
	 * Test of tokenize method, of class Token.
	 */
	@Test
	public void testTokenize() {
		String[] cases = {
				"",
				"aSingleToken",
				"a b c",
				"an's g'ri # quotation test",
				"Shell instance = new Shell(new ShellTest(), System.out",
				"dir \"E:\\ASG\\!dynamic\\projects\" \t-l 3492.9  ",
				"a b c ''",
				" \\\" ",
				"--b bValue --a aValue",
				"command1 param1 && command2 param2",
				"command1 param1 ; command2 param2"
			};

		int[] sizes = { 0, 1, 3, 1, 7, 4, 4, 1, 4, 5, 5};

		for (int i = 0; i < cases.length; i++) {
			List<Token> result = Token.tokenize(cases[i]);
			assertEquals(sizes[i], result.size());
		}
	}

	@Test
	public void testTokenEscaping() {
		Map<String, String> cases = new HashMap<String, String>();
		cases.put("\"para\\\"m\"", "para\"m");
		cases.put("'para\\'m'", "para'm");
		cases.put("\\\"param ", "\"param");
		cases.put("\\'param ", "'param");
		cases.put("param\\\"", "param\"");
		cases.put("param\\'", "param'");
		cases.put(" \\\" ", "\"");
		cases.put(" \\\' ", "\'");
		cases.put("\\\" ", "\"");
		cases.put("\\\' ", "\'");
		cases.put("--arg", "--arg");
		cases.put("\\\"--arg\\\"", "\"--arg\"");

		for (Entry<String, String> curCase : cases.entrySet()) {
			List<Token> result = Token.tokenize(curCase.getKey());
			assertEquals(curCase.getValue(), result.get(0).getString());
		}
	}

	/**
	 * Test of escapeString method, of class Token.
	 */
	@Test
	public void testEscapeString() {
		String[] cases = {
				"aSingleToken",
				"a b c",
				"an's g'ri # quotation test",
				"Shell instance = new Shell(new ShellTest(), System.out",
				"dir \"E:\\ASG\\!dynamic\\projects\" \t-l 3492.9  ",
				"param1 \"par\\\"am2\"", "param1 'par\\'am2'", "\\\"param",
				"'param",
				"'",
				"\"",
				"\\'",
				"\\\""
			};

		for (int i = 0; i < cases.length; i++) {
			String escaped = Token.escapeString(cases[i]);
			List<Token> result = Token.tokenize(escaped);

			assertEquals(1, result.size());
			assertEquals(cases[i], result.get(0).getString());
		}
	}

}