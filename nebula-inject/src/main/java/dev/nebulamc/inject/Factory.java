package dev.nebulamc.inject;

import java.lang.annotation.Documented;
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
 * class DatabaseFactory {
 *     @Service
 *     Database connectToDatabase()  {
 *         return Database.connect(...);
 *     }
 * }
 * }</pre>
 * <p>
 * Factory classes can be registered to a {@link Container} via
 * {@link Container.Builder#factory(Object)}:
 * <pre>{@code
 * Container container = Container.builder()
 *        .factory(new DatabaseFactory())
 *        .build();
 * }</pre>
 *
 * @author Sparky983
 * @see Service
 * @see Container.Builder
 * @since 1.0
 */
@Documented
@Retention(RUNTIME)
public @interface Factory {

}
