package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;

public class Echo extends IntegrationsTest {

	@Test
	public void echo() throws IOException{
		CommandResult result =  executeAndWaitForCommand(".echo", "hello ", "world");

		assertTrue(result.toString(), result.containsOutLine("hello world"));
	}

	@Test
	public void echoError() throws IOException{
		CommandResult result =  executeAndWaitForCommand(".echo-error", "hello ", "world");

		assertTrue(result.toString(), result.containsErrLine("hello world"));
	}

	@Test
	public void echoBuilder() throws IOException{
		executeCommand(".echo-build");
		executeCommand("normal", "hello ");
		executeCommand("normal", "world");
		CommandResult result = executeAndWaitForCommand("complete");

		assertTrue(result.toString(), result.containsOutLine("hello world"));
	}

	@Test
	public void echoErrorBuilder() throws IOException{
		executeCommand(".echo-error-build");
		executeCommand("normal", "hello ");
		executeCommand("normal", "world");
		CommandResult result = executeAndWaitForCommand("complete");

		assertTrue(result.toString(), result.containsErrLine("hello world"));
	}

	@Test
	public void echoBuilderColor() throws IOException, InterruptedException{
		executeCommand(".echo-build");
		executeCommand("black", "color");
		executeCommand("red", "color");
		executeCommand("green", "color");
		executeCommand("yellow", "color");
		executeCommand("blue", "color");
		executeCommand("magenta", "color");
		executeCommand("cyan", "color");
		executeCommand("white", "color");
		executeCommand("black-background", "color");
		executeCommand("red-background", "color");
		executeCommand("green-background", "color");
		executeCommand("yellow-background", "color");
		executeCommand("blue-background", "color");
		executeCommand("magenta-background", "color");
		executeCommand("cyan-background", "color");
		executeCommand("white-background", "color");
		executeCommand("complete");
		Thread.sleep(500);

		CommandResult result = waitForShell();

		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[30mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[31mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[32mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[33mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[34mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[35mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[36mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[37mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[40mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[41mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[42mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[43mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[44mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[45mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[46mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[47mcolor\u001B\\[0m.*"));
	}

	@Test
	public void echoErrorBuilderColor() throws IOException, InterruptedException{
		executeCommand(".echo-error-build");
		executeCommand("black", "color");
		executeCommand("red", "color");
		executeCommand("green", "color");
		executeCommand("yellow", "color");
		executeCommand("blue", "color");
		executeCommand("magenta", "color");
		executeCommand("cyan", "color");
		executeCommand("white", "color");
		executeCommand("black-background", "color");
		executeCommand("red-background", "color");
		executeCommand("green-background", "color");
		executeCommand("yellow-background", "color");
		executeCommand("blue-background", "color");
		executeCommand("magenta-background", "color");
		executeCommand("cyan-background", "color");
		executeCommand("white-background", "color");
		executeCommand("complete");
		Thread.sleep(500);

		CommandResult result = waitForShell();

		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[30mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[31mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[32mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[33mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[34mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[35mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[36mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[37mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[40mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[41mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[42mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[43mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[44mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[45mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[46mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsErrLine(".*\u001B\\[47mcolor\u001B\\[0m.*"));
	}

	@Test
	public void echoBuilderColorShow() throws IOException, InterruptedException{
		executeCommand(".echo-build");
		executeCommand("black", "color");
		executeCommand("red", "color");
		executeCommand("green", "color");
		executeCommand("yellow", "color");
		executeCommand("blue", "color");
		executeCommand("magenta", "color");
		executeCommand("cyan", "color");
		executeCommand("white", "color");
		executeCommand("black-background", "color");
		executeCommand("red-background", "color");
		executeCommand("green-background", "color");
		executeCommand("yellow-background", "color");
		executeCommand("blue-background", "color");
		executeCommand("magenta-background", "color");
		executeCommand("cyan-background", "color");
		executeCommand("white-background", "color");
		executeCommand("show");
		Thread.sleep(500);

		CommandResult result = waitForShell();

		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[30mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[31mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[32mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[33mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[34mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[35mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[36mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[37mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[40mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[41mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[42mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[43mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[44mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[45mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[46mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[47mcolor\u001B\\[0m.*"));
	}

	@Test
	public void echoErrorBuilderColorShow() throws IOException, InterruptedException{
		executeCommand(".echo-error-build");
		executeCommand("black", "color");
		executeCommand("red", "color");
		executeCommand("green", "color");
		executeCommand("yellow", "color");
		executeCommand("blue", "color");
		executeCommand("magenta", "color");
		executeCommand("cyan", "color");
		executeCommand("white", "color");
		executeCommand("black-background", "color");
		executeCommand("red-background", "color");
		executeCommand("green-background", "color");
		executeCommand("yellow-background", "color");
		executeCommand("blue-background", "color");
		executeCommand("magenta-background", "color");
		executeCommand("cyan-background", "color");
		executeCommand("white-background", "color");
		executeCommand("show");
		Thread.sleep(500);

		CommandResult result = waitForShell();

		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[30mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[31mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[32mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[33mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[34mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[35mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[36mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[37mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[40mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[41mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[42mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[43mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[44mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[45mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[46mcolor\u001B\\[0m.*"));
		assertTrue(result.toString(), result.containsOutLine(".*\u001B\\[47mcolor\u001B\\[0m.*"));
	}

	@Test
	public void echoBuilderClear() throws IOException{
		executeCommand(".echo-build");
		executeCommand("normal", "hello ");
		executeCommand("normal", "world");
		executeCommand("clear");
		CommandResult result = executeAndWaitForCommand("complete");

		assertFalse(result.toString(), result.containsOutLine("hello world"));
	}

	@Test
	public void echoErrorBuilderClear() throws IOException{
		executeCommand(".echo-error-build");
		executeCommand("normal", "hello ");
		executeCommand("normal", "world");
		executeCommand("clear");
		CommandResult result = executeAndWaitForCommand("complete");

		assertFalse(result.toString(), result.containsErrLine("hello world"));
	}
}
