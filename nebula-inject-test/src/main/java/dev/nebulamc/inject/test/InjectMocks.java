package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Container;
import dev.nebulamc.inject.Inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * An annotation that indicates that the annotated class should be injected with {@link Mock Mocks}.
 * <p>
 * The annotated field will be instantiated and injected by the test's {@link Container} with all
 * {@link Mock} fields:
 * <pre>{@code
 * class InjectMocksTest {
 *     @Mock Engine engine;
 *     @InjectMocks Car car;
 *     @Test
 *     void start() {
 *         car.start();
 *         verify(engine).start();
 *     }
 * }
 * }</pre>
 * <p>
 * Annotated fields must be:
 * <ul>
 *     <li>concrete</li>
 *     <li>non-final</li>
 *     <li>not annotated with {@link Mock}</li>
 * </ul>
 * This annotation is a replacement for {@link org.mockito.InjectMocks}. Unlike the aforementioned
 * annotation, this annotation only supports {@link Inject}.
 *
 * @author Sparky983
 * @see Mock
 * @since 1.0
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface InjectMocks {

}
