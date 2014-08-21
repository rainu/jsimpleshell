package de.raysha.lib.jsimpleshell.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.raysha.lib.jsimpleshell.Shell;

/**
 * This annotation can be used for dependency injection. If you want to
 * get access to {@link Shell} you can annotate a {@link Shell}-Field with
 * this annotation. Or you can annotate a method which have a {@link Shell}-Parameter.
 * The {@link Shell} will inject the dependency automatically.
 * If you have annotated a field or method which the {@link Shell} can not resolve, a
 * RuntimeException will be thrown!
 * <br />
 * <b>Note:</b> You can also use the {@link javax.inject.Inject} annotation if you
 * have these in you classpath.
 *
 * @author rainu
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inject {

}
