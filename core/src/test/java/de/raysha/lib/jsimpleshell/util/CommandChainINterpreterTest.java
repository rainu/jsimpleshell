package de.raysha.lib.jsimpleshell.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.Token;
import de.raysha.lib.jsimpleshell.util.CommandChainInterpreter.CommandLine;

public class CommandChainINterpreterTest {

	@Test
	public void singleLine(){
		List<Token> tokens = createTokens("command", "param1", "param2");

		List<CommandLine> result = CommandChainInterpreter.interpretTokens(tokens);

		assertEquals(1, result.size());
		assertTrue(result.get(0).isOr());
		assertEquals(tokens.size(), result.get(0).getTokens().size());
		for(int i=0; i < tokens.size(); i++){
			assertEquals(tokens.get(i).getString(),
					result.get(0).getTokens().get(i).getString());
		}
	}

	@Test
	public void andLine(){
		List<Token> tokens = createTokens("command1", CommandChainInterpreter.OPERATION_AND, "command2");

		List<CommandLine> result = CommandChainInterpreter.interpretTokens(tokens);

		assertEquals(2, result.size());
		assertTrue(result.get(0).isOr());
		assertTrue(result.get(1).isAnd());
		assertEquals(1, result.get(0).getTokens().size());
		assertEquals(1, result.get(1).getTokens().size());
		assertEquals("command1", result.get(0).getTokens().get(0).getString());
		assertEquals("command2", result.get(1).getTokens().get(0).getString());
	}

	@Test
	public void andLineWithParameter(){
		List<Token> tokens = createTokens("c1", "p1", CommandChainInterpreter.OPERATION_AND, "c2", "p2");

		List<CommandLine> result = CommandChainInterpreter.interpretTokens(tokens);

		assertEquals(2, result.size());
		assertTrue(result.get(0).isOr());
		assertTrue(result.get(1).isAnd());
		assertEquals(2, result.get(0).getTokens().size());
		assertEquals(2, result.get(1).getTokens().size());
		assertEquals("c1", result.get(0).getTokens().get(0).getString());
		assertEquals("p1", result.get(0).getTokens().get(1).getString());
		assertEquals("c2", result.get(1).getTokens().get(0).getString());
		assertEquals("p2", result.get(1).getTokens().get(1).getString());
	}

	@Test
	public void orLine(){
		List<Token> tokens = createTokens("command1", CommandChainInterpreter.OPERATION_OR, "command2");

		List<CommandLine> result = CommandChainInterpreter.interpretTokens(tokens);

		assertEquals(2, result.size());
		assertTrue(result.get(0).isOr());
		assertTrue(result.get(1).isOr());
		assertEquals(1, result.get(0).getTokens().size());
		assertEquals(1, result.get(1).getTokens().size());
		assertEquals("command1", result.get(0).getTokens().get(0).getString());
		assertEquals("command2", result.get(1).getTokens().get(0).getString());
	}

	@Test
	public void orLineWithParameter(){
		List<Token> tokens = createTokens("c1", "p1", CommandChainInterpreter.OPERATION_OR, "c2", "p2");

		List<CommandLine> result = CommandChainInterpreter.interpretTokens(tokens);

		assertEquals(2, result.size());
		assertTrue(result.get(0).isOr());
		assertTrue(result.get(1).isOr());
		assertEquals(2, result.get(0).getTokens().size());
		assertEquals(2, result.get(1).getTokens().size());
		assertEquals("c1", result.get(0).getTokens().get(0).getString());
		assertEquals("p1", result.get(0).getTokens().get(1).getString());
		assertEquals("c2", result.get(1).getTokens().get(0).getString());
		assertEquals("p2", result.get(1).getTokens().get(1).getString());
	}

	private List<Token> createTokens(String...strings) {
		List<Token> tokens = new ArrayList<Token>(strings.length);

		for(String s : strings){
			tokens.add(new Token(0, s));
		}

		return tokens;
	}
}
