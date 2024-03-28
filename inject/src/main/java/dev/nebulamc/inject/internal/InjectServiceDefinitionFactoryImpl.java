package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.nullness.NullMarked;

/**
 * A factory for creating {@link InjectServiceDefinition InjectServiceDefinitions}.
 *
 * @author Sparky983
 * @see InjectServiceDefinition
 */
@NullMarked
public final class InjectServiceDefinitionFactoryImpl implements InjectServiceDefinitionFactory {

    private final ParameterResolver parameterResolver;

    public InjectServiceDefinitionFactoryImpl(final ParameterResolver parameterResolver) {

        Preconditions.requireNonNull(parameterResolver, "parameterResolver");

        this.parameterResolver = parameterResolver;
    }

    @Override
    public <T> ServiceDefinition<T> createServiceDefinition(
            final Class<T> type,
            final Class<? extends T> implementation) {

        Preconditions.requireNonNull(type, "implementation");
        Preconditions.requireNonNull(implementation, "implementation");

        return new InjectServiceDefinition<>(type, implementation, parameterResolver);
    }
}
