package dev.nebulamc.inject.test;

import org.jspecify.nullness.NullMarked;

import static org.mockito.Mockito.mock;

import dev.nebulamc.inject.util.Preconditions;

/**
 * A {@link MockFactory} implementation that uses Mockito to create mock objects.
 *
 * @author Sparky983
 */
@NullMarked
final class MockitoMockFactory implements MockFactory {

    @Override
    public <T> T createMock(final Class<T> type, final Mock options) {

        Preconditions.requireNonNull(type, "type");
        Preconditions.requireNonNull(options, "options");

        return mock(type, options.answer());
    }
}
