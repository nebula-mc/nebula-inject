package dev.nebulamc.inject.test;

import org.jspecify.nullness.NullMarked;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Objects;

/**
 * A {@link ParameterResolver} implementation that resolves parameters annotated with {@link Mock}.
 */
@NullMarked
final class MockParameterResolver implements ParameterResolver {

    private final MockFactory mockFactory;

    /**
     * Extension constructor for JUnit.
     */
    MockParameterResolver() {

        this(new MockitoMockFactory());
    }

    MockParameterResolver(final MockFactory mockFactory) {

        Objects.requireNonNull(mockFactory, "mockFactory cannot be null");

        this.mockFactory = mockFactory;
    }
    
    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext)
            throws ParameterResolutionException {

        Objects.requireNonNull(parameterContext, "parameterContext cannot be null");
        Objects.requireNonNull(extensionContext, "extensionContext cannot be null");

        return parameterContext.isAnnotated(Mock.class);
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext)
            throws ParameterResolutionException {

        Objects.requireNonNull(parameterContext, "parameterContext cannot be null");
        Objects.requireNonNull(extensionContext, "extensionContext cannot be null");

        final Mock options = parameterContext.findAnnotation(Mock.class)
                .orElseThrow(IllegalArgumentException::new);

        return mockFactory.createMock(parameterContext.getParameter().getType(), options);
    }
}
