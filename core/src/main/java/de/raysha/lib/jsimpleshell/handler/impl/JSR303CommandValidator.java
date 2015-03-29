package de.raysha.lib.jsimpleshell.handler.impl;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;
import de.raysha.lib.jsimpleshell.handler.CommandValidator;

/**
 * This {@link CommandValidator} use the default JSR303 provider
 * (must be included in the classpath). This provider is responsible for
 * the javax.validation-annotations.
 *
 * @author rainu
 */
public class JSR303CommandValidator implements CommandValidator {
	private Validator validator;

	@Override
	public ValidationResult validate(Context ctx) {
		if(!usedJSR303Annotations(ctx)) return ValidationResult.forContext(ctx);

		Set<ConstraintViolation<Object>> violations = getValidator().forExecutables().validateParameters(
				ctx.getCommand().getHandler(),
				ctx.getCommand().getMethod(),
				ctx.getCommandParameters());

		final ValidationResult result = buildResult(ctx, violations);
		return result;
	}

	private ValidationResult buildResult(Context ctx, Set<ConstraintViolation<Object>> violations) {
		ValidationResult result = ValidationResult.forContext(ctx);

		for(ConstraintViolation<Object> violation : violations){
			ShellCommandParamSpec spec = determineSpec(ctx, violation);

			result.addFailure(spec, violation.getMessage());
		}

		return result;
	}

	private static final Pattern pathIndexPattern = Pattern.compile(".*arg([0-9]{1,})$");
	private ShellCommandParamSpec determineSpec(Context ctx, ConstraintViolation<Object> violation) {
		final String path = violation.getPropertyPath().toString();

		Matcher m = pathIndexPattern.matcher(path);
		m.find();

		final int index = Integer.parseInt(m.group(1));
		return ctx.getCommand().getParamSpecs()[index];
	}

	private boolean usedJSR303Annotations(Context ctx) {
		final Annotation[][] annotations = ctx.getCommand().getMethod().getParameterAnnotations();

		for(Annotation[] paramAnnotations : annotations){
			for(Annotation annotation : paramAnnotations){
				final String name = annotation.annotationType().getName();

				if(name.startsWith("javax.validation.")){
					return true;
				}
			}
		}

		return false;
	}

	public Validator getValidator() {
		if(validator == null){
			validator = Validation.buildDefaultValidatorFactory().getValidator();
		}

		return validator;
	}
}
