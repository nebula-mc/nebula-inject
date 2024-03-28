package dev.nebulamc.inject.test;

import org.jspecify.nullness.NullMarked;

/**
 * A factory for creating mock objects.
 *
 * @author Sparky983
 */
@NullMarked
interface MockFactory {

    /**
     * Creates a mock object with the specified options.
     *
     * @param type the type of the mock object.
     * @param options the options for the mock object.
     * @return the mock object.
     * @param <T> the type of the mock object.
     * @throws RuntimeException if an error occurs while creating the mock object.
     */
    <T> T createMock(final Class<T> type, final Mock options);
}
