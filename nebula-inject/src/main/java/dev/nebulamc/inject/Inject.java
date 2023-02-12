package dev.nebulamc.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to mark an
 * <a href="package-summary.html#injectable-constructors">injectable constructor</a>.
 *
 * @author Sparky983
 * @see <a href="package-summary.html#injectable-constructors">injectable constructor</a>
 * @since 1.0
 */
@Documented
@Retention(RUNTIME)
@Target(CONSTRUCTOR)
public @interface Inject {

}
