package de.raysha.lib.jsimpleshell.it.special;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Locale;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.CommandResult;
import de.raysha.lib.jsimpleshell.IntegrationsTest;
import de.raysha.lib.jsimpleshell.ValidationCommands;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.raysha.lib.jsimpleshell.handler.CommandValidator;
import de.raysha.lib.jsimpleshell.handler.impl.AbstractMessageResolver;

public class Validation extends IntegrationsTest {
	private static final String FAIL_MESSAGE = "message.validation.FAIL";

	@Override
	protected ShellBuilder buildShell() throws IOException {
		return super.buildShell()
					.behavior()
						.addHandler(new ValidationCommands())
						.addHandler(new MyCommandValidator())
						.addHandler(new MyMessageResolver())
					.root();
	}

	private class MyMessageResolver extends AbstractMessageResolver {
		@Override
		protected String resolveMessage(String msg) {
			if(FAIL_MESSAGE.equals(msg)){
				return "Resolved failure message";
			}

			return msg;
		}

		@Override
		public boolean supportsLocale(Locale locale) {
			return true;
		}
	}

	private class MyCommandValidator implements CommandValidator {
		@Override
		public ValidationResult validate(Context context) {
			Object[] values = context.getCommandParameters();

			if(values.length > 0 && "1312".equals(values[0])){
				return ValidationResult.forContext(context)
						.addFailure(context.getCommand().getParamSpecs()[0], FAIL_MESSAGE);
			}

			return ValidationResult.forContext(context);
		}
	}

	@Test
	public void patternMatch() throws IOException{
		CommandResult result = executeAndWaitForCommand("validate", "123");

		assertFalse(result.isError());
		assertTrue(result.toString(), result.containsLine("Parameter: 123.*"));
	}

	@Test
	public void patternMismatch() throws IOException{
		CommandResult result = executeAndWaitForCommand("validate", "test");

		assertTrue(result.isError());
		assertTrue(result.toString(), result.containsErrLine("p1: must match.*"));
	}

	@Test
	public void customValidator() throws IOException{
		CommandResult result = executeAndWaitForCommand("validate", "1312");

		assertTrue(result.isError());
		assertFalse(result.toString(), result.containsErrLine("p1: must match.*"));
		assertTrue(result.toString(), result.containsErrLine("p1: Resolved failure message"));
	}
}
