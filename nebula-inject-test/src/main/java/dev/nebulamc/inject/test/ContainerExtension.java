package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Container;
import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.NoUniqueServiceException;
import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceException;
import dev.nebulamc.inject.ServiceFinder;
import dev.nebulamc.inject.internal.ContainerImpl;
import dev.nebulamc.inject.internal.FallbackServiceDefinitionRegistry;
import dev.nebulamc.inject.internal.ServiceFinderServiceDefinitionRegistry;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The extension that creates the container and creates and injects mocks to {@link Mock} and
 * {@link Inject} fields.
 * <p>
 * Does not include {@link MockParameterResolver}.
 *
 * @author Sparky983
 */
@NullMarked
final class ContainerExtension
        implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private final MockFactory mockFactory;

    private @Nullable NebulaInjectTests tests;

    /**
     * The container to use for parameter resolution.
     */
    private @Nullable Container container;

    /**
     * Extension constructor for JUnit.
     */
    ContainerExtension() {

        this(new MockitoMockFactory());
    }

    /**
     * Testing constructor.
     *
     * @param mockFactory the mock factory to create mocks for {@link Mock} fields.
     * @throws NullPointerException if mockFactory is {@code null}.
     */
    ContainerExtension(final MockFactory mockFactory) {

        Preconditions.requireNonNull(mockFactory, "mockFactory");

        this.mockFactory = mockFactory;
    }

    @Override
    public void beforeAll(final ExtensionContext context) {

        Preconditions.requireNonNull(context, "context cannot be null");

        tests = new NebulaInjectTests(context.getRequiredTestClass());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {

        Preconditions.requireNonNull(context, "context");

        if (tests == null) {
            throw new IllegalStateException("beforeAll(ExtensionContext) has not been called yet");
        }

        if (container != null) {
            throw new IllegalStateException("beforeEach(ExtensionContext) has already been called");
        }

        final Container.Builder testDoublesBuilder = Container.builder();

        for (final Field field : tests.getMockFields()) {
            final Object mock = mockFactory.createMock(
                    field.getType(), field.getAnnotation(Mock.class));
            field.setAccessible(true);
            try {
                field.set(context.getRequiredTestInstance(), mock);
            } finally {
                field.setAccessible(false);
            }
            testDoublesBuilder.singleton(mock, (Class) field.getType());
        }

        for (final Field field : tests.getServiceFields()) {
            field.setAccessible(true);
            try {
                final Object service = field.get(context.getRequiredTestInstance());
                testDoublesBuilder.singleton(service, (Class) field.getType());
            } finally {
                field.setAccessible(false);
            }
        }

        for (final Method method : tests.getServiceMethods()) {
            testDoublesBuilder.serviceDefinition(new ServiceDefinition<>() {
                @Override
                public Class getServiceType() {

                    return method.getReturnType();
                }

                @Override
                public Object createService(final ServiceFinder serviceFinder) {

                    return context.getExecutableInvoker()
                            .invoke(method, context.getRequiredTestInstance());
                }
            });
        }

        final Container testDoubles = testDoublesBuilder.build();

        final Container.Builder builder = Container.builder();

        for (final Field field : tests.getFactoryFields()) {
            final Object factory = testDoubles.findService(field.getType());
            field.setAccessible(true);
            try {
                field.set(context.getRequiredTestInstance(), factory);
            } finally {
                field.setAccessible(false);
            }
            builder.factory(factory);
        }

        container = new ContainerImpl(
                new FallbackServiceDefinitionRegistry(new ServiceFinderServiceDefinitionRegistry(testDoubles), builder.build()));

        for (final Field field : tests.getInjectFields()) {
            field.setAccessible(true);
            try {
                field.set(context.getRequiredTestInstance(), container.findService(field.getType()));
            } finally {
                field.setAccessible(false);
            }
        }
    }

    @Override
    public void afterEach(final ExtensionContext context) throws Exception {

        Preconditions.requireNonNull(context, "context");

        if (tests == null) {
            throw new IllegalStateException("beforeAll(ExtensionContext) has not been called yet");
        }

        if (container == null) {
            throw new IllegalStateException("beforeEach(ExtensionContext) has not been called yet");
        }

        container = null;
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext)
            throws ParameterResolutionException {

        Preconditions.requireNonNull(parameterContext, "parameterContext");
        Preconditions.requireNonNull(extensionContext, "extensionContext");

        return parameterContext.isAnnotated(Inject.class);
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext)
            throws ParameterResolutionException {

        Preconditions.requireNonNull(parameterContext, "parameterContext");
        Preconditions.requireNonNull(extensionContext, "extensionContext");

        if (tests == null) {
            throw new IllegalStateException("beforeAll(ExtensionContext) has not been called yet");
        }

        if (container == null) {
            throw new IllegalStateException("beforeEach(ExtensionContext) has not been called yet");
        }

        if (!supportsParameter(parameterContext, extensionContext)) {
            throw new IllegalArgumentException("resolveParameter(ParameterContext, ExtensionContext) "
                    + "should only be called if " +
                    "supportsParameter(ParameterContext, ExtensionContext) returns true");
        }

        try {
            return container.findService(parameterContext.getParameter().getType());
        } catch (final NoUniqueServiceException | ServiceException e) {
            throw new ParameterResolutionException(e.getMessage(), e);
        }
    }
}
