package dev.nebulamc.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a method of a {@link Factory} class as a service method.
 * <p>
 * The annotated method must be public and non-static, and must return a non-{@code null} value.
 * <p>
 * Service methods are allowed to have parameters which will be injected by the container:
 * <pre>{@code
 * @Service
 * public Car createCar(Engine engine) {
 *     return new Car(engine);
 * }
 * }</pre>
 *
 * @author Sparky983
 * @see Factory
 * @since 1.0
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Service {

}
