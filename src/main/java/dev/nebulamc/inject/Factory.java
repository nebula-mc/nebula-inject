package dev.nebulamc.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a class as a service factory that contains {@link Service} methods.
 * <p>
 * The main purpose of service factories that they can provide services that don't
 * support Nebula Inject such as external objects, for example:
 * <pre>{@code
 * @Factory
 * public class DatabaseFactory {
 *     @Service
 *     public Database connectToDatabase()  {
 *         return Database.connect(...);
 *     }
 * }
 * }</pre>
 *
 * @author Sparky983
 * @see Service
 * @see Container.Builder#factory(Object)
 * @since 1.0
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Factory {

}
