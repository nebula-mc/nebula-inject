package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.lang.reflect.Method;

/**
 * The main implementation of {@link FactoryServiceDefinitionRegistryFactory}.
 *
 * @author Sparky983
 */
@NullMarked
final class FactoryServiceDefinitionRegistryFactoryImpl
        implements FactoryServiceDefinitionRegistryFactory {

    private final ServiceServiceDefinitionFactory serviceServiceDefinitionFactory;

    FactoryServiceDefinitionRegistryFactoryImpl(
            final ServiceServiceDefinitionFactory serviceServiceDefinitionFactory) {

        Preconditions.requireNonNull(
                serviceServiceDefinitionFactory, "serviceServiceDefinitionFactory");

        this.serviceServiceDefinitionFactory = serviceServiceDefinitionFactory;
    }

    @Override
    public ServiceDefinitionRegistry createServiceDefinitionRegistry(final Object factory) {

        Preconditions.requireNonNull(factory, "factory");

        final ServiceDefinitionRegistry.Builder builder = ServiceDefinitionRegistry.builder();

        if (!factory.getClass().isAnnotationPresent(Factory.class)) {
            throw new IllegalArgumentException("The factory must be annotated with @Factory.");
        }

        for (final Method method : factory.getClass().getMethods()) {
            if (method.isAnnotationPresent(Service.class)) {
                builder.serviceDefinition(
                        serviceServiceDefinitionFactory.createServiceDefinition(factory, method));
            }
        }

        return builder.build();
    }
}
