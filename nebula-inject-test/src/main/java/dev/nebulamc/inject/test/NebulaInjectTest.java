package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Container;
import dev.nebulamc.inject.Factory;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Enables testing for Nebula Inject.
 * <p>
 * This enables the {@link Mock} and {@link InjectMocks} annotations.
 * <p>
 * In addition, this enables the {@link Factory} annotation for defining factories. Annotated fields
 * will be injected with {@link Mock}s and then be added the test's {@link Container}.
 *
 * @author Sparky983
 * @since 1.0
 */
@ExtendWith({ContainerExtension.class, MockParameterResolver.class})
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface NebulaInjectTest {

}
