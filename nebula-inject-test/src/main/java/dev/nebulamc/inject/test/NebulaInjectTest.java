package dev.nebulamc.inject.test;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Enables the {@link Mock} and {@link InjectMocks} annotations to be used in a test class.
 *
 * @author Sparky983
 * @since 1.0
 */
@ExtendWith(ContainerExtension.class)
@ExtendWith(MockParameterResolver.class)
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface NebulaInjectTest {

}
