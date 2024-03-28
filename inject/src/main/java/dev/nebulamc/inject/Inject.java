package dev.nebulamc.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to mark an
 * <a href="package-summary.html#injectable-constructors">injectable constructor</a>.
 *
 * @author Sparky983
 * @see <a href="package-summary.html#injectable-constructors">injectable constructor</a>
 * @since 0.1
 */
@Documented
@Retention(RUNTIME)
public @interface Inject {

}
