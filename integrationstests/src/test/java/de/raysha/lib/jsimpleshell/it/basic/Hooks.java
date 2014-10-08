package de.raysha.lib.jsimpleshell.it.basic;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import de.raysha.lib.jsimpleshell.ConverterCommands;
import de.raysha.lib.jsimpleshell.ConverterCommands.Entry;
import de.raysha.lib.jsimpleshell.ExitAlternativeCommands;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.ShellCommand;
import de.raysha.lib.jsimpleshell.SubShellCommands;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.exception.ExitException;
import de.raysha.lib.jsimpleshell.handler.CommandHookDependent;
import de.raysha.lib.jsimpleshell.handler.CommandHookDependent.ExecutionResult;

public class Hooks extends IntegrationsTest {

	private CommandHookDependent hookMock;

	@Override
	public void setup() throws IOException{
		hookMock = Mockito.mock(CommandHookDependent.class);

		super.setup();
	}

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
					.behavior()
						.addHandler(new SubShellCommands())
						.addHandler(new ConverterCommands())
						.addAuxHandler(this.hookMock)
					.back();
	}

	@Test
	public void commandHook() throws IOException{
		executeAndWaitForCommand("to-entry key value");

		ArgumentCaptor<ShellCommand> cmdCap = ArgumentCaptor.forClass(ShellCommand.class);
		ArgumentCaptor<Object[]> paramCap = ArgumentCaptor.forClass(Object[].class);
		ArgumentCaptor<ExecutionResult> resultCap = ArgumentCaptor.forClass(ExecutionResult.class);

		verify(hookMock, times(1)).cliBeforeCommand(cmdCap.capture(), paramCap.capture());
		verify(hookMock, times(1)).cliAfterCommand(cmdCap.capture(), paramCap.capture(), resultCap.capture());

		assertEquals("to-entry", cmdCap.getAllValues().get(0).getName());
		assertEquals("to-entry", cmdCap.getAllValues().get(1).getName());

		assertTrue(Arrays.equals(paramCap.getAllValues().get(0), new Object[]{"key", "value"}));
		assertTrue(Arrays.equals(paramCap.getAllValues().get(0), paramCap.getAllValues().get(0)));

		assertTrue(resultCap.getValue().wasExecutionSuccessful());
		assertNull(resultCap.getValue().getThrown());
		assertFalse(resultCap.getValue().getExecutionTime() == 0L);
		assertTrue(resultCap.getValue().getResult() instanceof Entry);
		assertEquals("key", ((Entry)resultCap.getValue().getResult()).getKey());
		assertEquals("value", ((Entry)resultCap.getValue().getResult()).getValue());
	}

	@Test
	public void commandHook_causeException() throws IOException{
		executeAndWaitForCommand("cause-exception");

		ArgumentCaptor<ShellCommand> cmdCap = ArgumentCaptor.forClass(ShellCommand.class);
		ArgumentCaptor<Object[]> paramCap = ArgumentCaptor.forClass(Object[].class);
		ArgumentCaptor<ExecutionResult> resultCap = ArgumentCaptor.forClass(ExecutionResult.class);

		verify(hookMock, times(1)).cliBeforeCommand(cmdCap.capture(), paramCap.capture());
		verify(hookMock, times(1)).cliAfterCommand(cmdCap.capture(), paramCap.capture(), resultCap.capture());

		assertEquals("cause-exception", cmdCap.getAllValues().get(0).getName());
		assertEquals("cause-exception", cmdCap.getAllValues().get(1).getName());

		assertTrue(Arrays.equals(paramCap.getAllValues().get(0), new Object[]{}));
		assertTrue(Arrays.equals(paramCap.getAllValues().get(0), paramCap.getAllValues().get(0)));

		assertFalse(resultCap.getValue().wasExecutionSuccessful());
		assertNull(resultCap.getValue().getResult());
		assertFalse(resultCap.getValue().getExecutionTime() == 0L);
		assertTrue(resultCap.getValue().getThrown() instanceof RuntimeException);
		assertEquals("Error!", ((RuntimeException)resultCap.getValue().getThrown()).getMessage());
	}

	@Test
	public void commandHook_exitAlternative() throws IOException {
		executeAndWaitForCommand("sub-shell-without-exit");
		executeAndWaitForCommand("quit");

		ArgumentCaptor<ShellCommand> cmdCap = ArgumentCaptor.forClass(ShellCommand.class);
		ArgumentCaptor<Object[]> paramCap = ArgumentCaptor.forClass(Object[].class);
		ArgumentCaptor<ExecutionResult> resultCap = ArgumentCaptor.forClass(ExecutionResult.class);

		verify(hookMock, times(2)).cliBeforeCommand(cmdCap.capture(), paramCap.capture());
		verify(hookMock, times(2)).cliAfterCommand(cmdCap.capture(), paramCap.capture(), resultCap.capture());

		assertEquals("quit", cmdCap.getAllValues().get(1).getName());
		assertEquals("quit", cmdCap.getAllValues().get(2).getName());

		assertTrue(Arrays.equals(paramCap.getAllValues().get(0), new Object[]{}));
		assertTrue(Arrays.equals(paramCap.getAllValues().get(0), paramCap.getAllValues().get(0)));

		assertTrue(resultCap.getAllValues().get(0).wasExecutionSuccessful());
		assertNull(resultCap.getAllValues().get(0).getResult());
		assertTrue(resultCap.getAllValues().get(0).getThrown() instanceof ExitException);
	}
}
