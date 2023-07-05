package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Container;
import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.util.Preconditions;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Field;

/**
 * The extension that creates the container and creates and injects mocks to {@link Mock} and
 * {@link Inject} fields.
 * <p>
 * Does not include {@link MockParameterResolver}.
 *
 * @author Sparky983
 */
@NullMarked
final class ContainerExtension implements BeforeAllCallback, BeforeEachCallback {

    private final MockFactory mockFactory;

    private @Nullable NebulaInjectTests tests;

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

        final Container.Builder mocksAndServicesBuilder = Container.builder();

        for (final Field field : tests.getMockFields()) {
            final Object mock = mockFactory.createMock(
                    field.getType(), field.getAnnotation(Mock.class));
            field.setAccessible(true);
            try {
                field.set(context.getRequiredTestInstance(), mock);
            } finally {
                field.setAccessible(false);
            }
            mocksAndServicesBuilder.singleton(mock, (Class) field.getType());
        }

        for (final Field field : tests.getServiceFields()) {
            field.setAccessible(true);
            try {
                final Object service = field.get(context.getRequiredTestInstance());
                mocksAndServicesBuilder.singleton(service, (Class) field.getType());
            } finally {
                field.setAccessible(false);
            }
        }

        final Container mocksContainer = mocksAndServicesBuilder.build();

        final Container.Builder builder = Container.builder()
                .parent(mocksContainer);

        for (final Field field : tests.getFactoryFields()) {
            final Object factory = mocksContainer.findService(field.getType());
            field.setAccessible(true);
            try {
                field.set(context.getRequiredTestInstance(), factory);
            } finally {
                field.setAccessible(false);
            }
            builder.factory(factory);
        }

        final Container container = builder.build();

        for (final Field field : tests.getInjectFields()) {
            field.setAccessible(true);
            try {
                field.set(context.getRequiredTestInstance(), container.findService(field.getType()));
            } finally {
                field.setAccessible(false);
            }
        }
    }
}
