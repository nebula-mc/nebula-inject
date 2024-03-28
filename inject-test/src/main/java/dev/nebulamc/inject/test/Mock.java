package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Inject;
import org.jspecify.nullness.NullMarked;
import org.mockito.Answers;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * An annotation that indicates that the annotated field or parameter should be mocked.
 * <p>
 * If this annotation is present on a field, it is added to the test's container before
 * {@link Inject} fields are, so it can be used by the {@link Inject} fields:
 * <pre>{@code
 * class MockTest {
 *     @Mock Engine engine;
 *     @Test
 *     void testEngine() {
 *         engine.start();
 *         verify(engine).start();
 *     }
 * }
 * }</pre>
 * <p>
 * When this annotation is present on a parameter, a mock object is created and passed into the
 * method, but it is not added to the container. This is useful for "on-demand mocks" where a mock
 * object is only needed for a single method call or test:
 * <pre>{@code
 * class MockTest {
 *     @Test
 *     void testEngine(@Mock Petrol petrol) {
 *         engine.fuel(petrol);
 *         verify(petrol).setContents(0);
 *     }
 * }
 * }</pre>
 * <p>
 * The annotated field or parameter must be mockable by {@link org.mockito.Mockito#mock(Class)},
 * meaning that is:
 * <ul>
 *     <li>non-final</li>
 *     <li>not a primitive</li>
 * </ul>
 *
 * @author Sparky983
 * @see Inject
 * @since 0.1
 */
@NullMarked
@Documented
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
public @interface Mock {

    /**
     * The answer to use when a method is called on the mock.
     *
     * @return the answer to use when a method is called on the mock
     */
    Answers answer() default Answers.RETURNS_DEFAULTS;
}
