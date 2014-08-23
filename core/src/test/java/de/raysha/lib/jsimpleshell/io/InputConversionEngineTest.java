/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.raysha.lib.jsimpleshell.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;
import de.raysha.lib.jsimpleshell.Token;
import de.raysha.lib.jsimpleshell.handler.InputConverter;
import de.raysha.lib.jsimpleshell.io.InputConversionEngine;

/**
 *
 * @author ASG
 */
public class InputConversionEngineTest implements InputConverter {

	@Before
	public void setUp() {
		converter = new InputConversionEngine();
	}

	@After
	public void tearDown() {
	}

	private InputConversionEngine converter;

	@Test
	public void testElementaryTypes() throws Exception {
		System.out.println("testElementaryTypes");
		String[] strings = {
				"aString",
				"some-object",
				"1243",
				"12432141231256",
				"12.6",
				"12.6",
				"true",
			};
		Class[] classes = {
				String.class,
				Object.class,
				Integer.class,
				Long.class,
				Double.class,
				Float.class,
				Boolean.class,
			};
		Object[] results = {
				"aString",
				(Object) "some-object",
				new Integer(1243),
				new Long(12432141231256l),
				new Double(12.6),
				new Float(12.6f),
				new Boolean(true),
			};

		for (int i = 0; i < strings.length; i++) {
			assertEquals(results[i],
					converter.convertInput(strings[i], classes[i]));
		}

	}

	private static final int MAGIC_INT = 234;

	private static final InputConverter testInputConverter = new InputConverter() {
		public Object convertInput(String original, Class toClass)
				throws Exception {
			if (toClass.equals(Integer.class)) {
				return MAGIC_INT; // no matter what was the text, for test purposes
			} else {
				return null;
			}
		}
	};

	public Object convertInput(String original, Class toClass) throws Exception {
		return testInputConverter.convertInput(original, toClass);
	}

	@Test
	public void testConverterRegistration() throws Exception {
		System.out.println("testConverterRegistration");
		InputConversionEngine otherConverter = new InputConversionEngine();
		otherConverter.addConverter(testInputConverter);
		assertEquals(new Integer(MAGIC_INT),
				otherConverter.convertInput("10", Integer.class));
		otherConverter.removeConverter(testInputConverter);
		assertEquals(new Integer(MAGIC_INT), otherConverter.convertInput(
				Integer.toString(MAGIC_INT), Integer.class));
	}

	@Test
	public void testDeclaredConverterRegistration() throws Exception {
		System.out.println("testDeclaredConverterRegistration");
		InputConversionEngine otherConverter = new InputConversionEngine();
		otherConverter.addDeclaredConverters(this);
		assertEquals(new Integer(MAGIC_INT),
				otherConverter.convertInput("10", Integer.class));
	}

	@Test
	public void orderTokens(){
		InputConversionEngine engine = new InputConversionEngine();

		ShellCommandParamSpec[] specs = new ShellCommandParamSpec[]{
			new ShellCommandParamSpec("arg1", null, null, 1, null, false),
			new ShellCommandParamSpec("arg2", null, null, 2, null, false),
			new ShellCommandParamSpec("varArg", null, null, 3, null, true)
		};

		List<Token> tokens = Arrays.asList(
			new Token(0, "cmd"), new Token(1, "--arg2"), new Token(2, "arg2Value"),
			new Token(3, "--varArg"), new Token(4, "varArg1"),
			new Token(5, "--arg1"), new Token(6, "arg1Value"),
			new Token(7, "--varArg"), new Token(8, "varArg2"),
			new Token(9, "--varArg"), new Token(10, "varArg3")
		);

		List<Token> result = engine.orderTokens(tokens, specs);
		assertEquals(6, result.size());
		assertSame(result.get(0), tokens.get(0));
		assertSame(result.get(1), tokens.get(6));
		assertSame(result.get(2), tokens.get(2));
		assertSame(result.get(3), tokens.get(4));
		assertSame(result.get(4), tokens.get(8));
		assertSame(result.get(5), tokens.get(10));
	}
}