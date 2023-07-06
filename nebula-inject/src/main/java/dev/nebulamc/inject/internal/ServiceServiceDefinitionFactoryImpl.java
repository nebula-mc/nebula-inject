package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.nullness.NullMarked;

import java.lang.reflect.Method;

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
