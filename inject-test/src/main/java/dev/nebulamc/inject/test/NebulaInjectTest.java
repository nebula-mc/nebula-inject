package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Container;
import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.Service;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Enables testing for Nebula Inject.
 * <p>
 * Enables the following annotations for use on fields in the annotated test:
 * <ul>
 *     <li>
 *         {@link Mock} - Adds a mock of the annotated field's type to the test's {@link Container}.
 *     </li>
 *     <li>
 *         {@link Factory} -
 *         Adds a factory of the annotated field's type to the test's {@link Container}.
 *     </li>
 *     <li>
 *         {@link Service} - To add the annotated field's value to the test's {@link Container}.
 *     </li>
 *     <li>
 *         {@link Inject} - To inject the annotated field's type.
 *     </li>
 * </ul>
 * <p>
 * If it is impossible to use {@link Service} on a field, it is possible to use a getter method.
 * <p>
 * The following annotations may additionally be used as parameter resolvers:
 * <ul>
 *     <li>{@link Mock} - Resolves to a new mock of the parameter's type.</li>
 *     <li>{@link Inject} - Resolves to the service in the test's {@link Container}.</li>
 * </ul>
 *
 * @author Sparky983
 * @since 0.1
 */
@ExtendWith({ContainerExtension.class, MockParameterResolver.class})
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface NebulaInjectTest {

}
