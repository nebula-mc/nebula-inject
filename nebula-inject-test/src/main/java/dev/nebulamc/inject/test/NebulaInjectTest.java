package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Container;
import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Inject;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Enables testing for Nebula Inject.
 * <p>
 * This enables the {@link Mock} annotation for the annotated test.
 * <p>
 * In addition, this enables the {@link Factory} annotation for defining factories. Annotated fields
 * will be injected with {@link Mock}s and then be added the test's {@link Container}.
 * <p>
 * Finally, you this enables the {@link Inject} annotation to be used to inject dependencies into
 * the annotated test.
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
