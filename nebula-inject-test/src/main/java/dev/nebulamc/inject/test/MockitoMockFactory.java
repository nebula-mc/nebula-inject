package dev.nebulamc.inject.test;

import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.annotations.NullMarked;

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

        Preconditions.requireNonNull(type, "type");
        Preconditions.requireNonNull(options, "options");

        return mock(type, options.answer());
    }
}
