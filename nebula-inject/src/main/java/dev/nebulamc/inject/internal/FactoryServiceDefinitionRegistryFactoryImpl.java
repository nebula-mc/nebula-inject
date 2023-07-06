package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Service;
import dev.nebulamc.inject.ServiceDefinitionRegistry;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.nullness.NullMarked;

import java.lang.reflect.Method;

/**
 * The main implementation of {@link FactoryServiceDefinitionRegistryFactory}.
 *
 * @author Sparky983
 */
@NullMarked
public final class FactoryServiceDefinitionRegistryFactoryImpl
        implements FactoryServiceDefinitionRegistryFactory {

    private final ServiceServiceDefinitionFactory serviceServiceDefinitionFactory;

    /**
     * Constructs a new {@link FactoryServiceDefinitionRegistryFactoryImpl} using the given {@link
     * ServiceServiceDefinitionFactory} as the factory to create service definitions for methods
     * annotated with {@link Service @Service}.
     *
     * @param serviceServiceDefinitionFactory the service service definition factory to use
     * @throws NullPointerException if {@code serviceServiceDefinitionFactory} is {@code null}.
     */
    public FactoryServiceDefinitionRegistryFactoryImpl(
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
            throw new IllegalArgumentException(factory.getClass().getName() +
                    " must be annotated with @" +
                    Factory.class.getName());
        }

        for (final Method method : factory.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Service.class)) {
                builder.serviceDefinition(
                        serviceServiceDefinitionFactory.createServiceDefinition(factory, method));
            }
        }

        return builder.build();
    }
}
