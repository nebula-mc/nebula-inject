package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Method;

/**
 * The main implementation of {@link ServiceServiceDefinitionFactory}.
 *
 * @author Sparky983
 */
@NullMarked
final class ServiceServiceDefinitionFactoryImpl implements ServiceServiceDefinitionFactory {

    private final ParameterResolver parameterResolver;

    public ServiceServiceDefinitionFactoryImpl(final ParameterResolver parameterResolver) {
        Preconditions.requireNonNull(parameterResolver, "parameterResolver");

        this.parameterResolver = parameterResolver;
    }

    @Override
    public ServiceDefinition<?> createServiceDefinition(final Object factory,
                                                        final Method serviceMethod) {

        Preconditions.requireNonNull(factory, "factory");
        Preconditions.requireNonNull(serviceMethod, "serviceMethod");

        return new ServiceServiceDefinition<>(factory, serviceMethod, parameterResolver);
    }
}
