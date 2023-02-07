package dev.nebulamc.inject.test;

import org.jspecify.nullness.NullMarked;

import java.util.Objects;

import static org.mockito.Mockito.mock;

/**
 * A {@link MockFactory} implementation that uses Mockito to create mock objects.
 *
 * @author Sparky983
 */
@NullMarked
final class MockitoMockFactory implements MockFactory {

    @Override
    public <T> T createMock(final Class<T> type, final Mock options) {

        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(options, "options cannot be null");

        return mock(type, options.answer());
    }
}
