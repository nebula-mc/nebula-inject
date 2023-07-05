package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.lang.reflect.Method;

import dev.nebulamc.inject.util.Preconditions;

/**
 * The main implementation of {@link ServiceServiceDefinitionFactory}.
 *
 * @author Sparky983
 */
@NullMarked
final class ServiceServiceDefinitionFactoryImpl implements ServiceServiceDefinitionFactory {

    @Override
    public ServiceDefinition<?> createServiceDefinition(final Object factory,
                                                        final Method serviceMethod) {

        Preconditions.requireNonNull(factory, "factory");
        Preconditions.requireNonNull(serviceMethod, "serviceMethod");

        return new ServiceServiceDefinition<>(factory, serviceMethod);
    }
}
