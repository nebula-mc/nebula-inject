package dev.nebulamc.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a method of a {@link Factory} class as a service method.
 * <p>
 * The annotated method must be public and non-static, and must return a non-{@code null} value.
 *
 * @author Sparky983
 * @see Factory
 * @since 1.0
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Service {

}
