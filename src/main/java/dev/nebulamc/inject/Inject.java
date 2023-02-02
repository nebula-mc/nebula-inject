package dev.nebulamc.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to mark an injectable constructor.
 *
 * @author Sparky983
 * @since 1.0
 */
@Retention(RUNTIME)
@Target(CONSTRUCTOR)
public @interface Inject {

}
