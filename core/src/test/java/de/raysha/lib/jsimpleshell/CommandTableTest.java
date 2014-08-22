package de.raysha.lib.jsimpleshell;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class CommandTableTest {

	@Test
	public void parameterCount(){
		CommandTable cmdTable = new CommandTable(null);

		assertEquals(0, cmdTable.parameterCount(Arrays.asList(
				new Token(0, "cmd"))));

		assertEquals(1, cmdTable.parameterCount(Arrays.asList(
				new Token(0, "cmd"), new Token(1, "param"))));

		assertEquals(2, cmdTable.parameterCount(Arrays.asList(
				new Token(0, "cmd"), new Token(1, "param1"), new Token(2, "param2"))));

		assertEquals(1, cmdTable.parameterCount(Arrays.asList(
				new Token(0, "cmd"), new Token(1, "--param"), new Token(2, "paramValue"))));

		assertEquals(2, cmdTable.parameterCount(Arrays.asList(
				new Token(0, "cmd"), new Token(1, "\"--param\""), new Token(2, "paramValue"))));
	}
}
