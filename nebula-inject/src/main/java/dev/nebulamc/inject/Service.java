package dev.nebulamc.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a method of a {@link Factory} class as a service method.
 * <p>
 * The annotated method must be and non-static, and must return a non-{@code null} value.
 * <p>
 * Service methods are allowed to have parameters which will be injected by the container:
 * <pre>{@code
 * @Service
 * Car createCar(Engine engine) {
 *     return new Car(engine);
 * }
 * }</pre>
 *
 * @author Sparky983
 * @see Factory
 * @since 0.1
 */
@Documented
@Retention(RUNTIME)
public @interface Service {

}
