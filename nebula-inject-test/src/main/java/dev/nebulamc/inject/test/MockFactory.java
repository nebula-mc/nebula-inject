package dev.nebulamc.inject.test;

import org.jspecify.nullness.NullMarked;

/**
 * A factory for creating mock objects.
 *
 * @author Sparky983
 */
@NullMarked
interface MockFactory {

    <T> T createMock(final Class<T> type, final Mock options);
}
