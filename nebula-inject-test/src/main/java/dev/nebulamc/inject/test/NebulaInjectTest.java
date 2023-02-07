package dev.nebulamc.inject.test;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Enables the {@link Mock} parameter resolver and the {@link InjectMocks} extension.
 *
 * @author Sparky983
 * @since 1.0
 */
@ExtendWith(ContainerExtension.class)
@ExtendWith(MockParameterResolver.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface NebulaInjectTest {

}
