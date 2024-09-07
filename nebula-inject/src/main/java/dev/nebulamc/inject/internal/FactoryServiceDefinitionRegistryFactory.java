package dev.nebulamc.inject.internal;

import org.jspecify.annotations.NullMarked;
import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Service;
import dev.nebulamc.inject.ServiceDefinitionRegistry;

/**
 * A factory for creating {@link ServiceDefinitionRegistry} objects for {@link Factory} classes.
 *
 * @author Sparky983
 */
@NullMarked
public interface FactoryServiceDefinitionRegistryFactory {

    /**
     * Creates a {@link ServiceDefinitionRegistry} of the specified {@link Factory} class'
     * {@link Service} methods.
     *
     * @param factory the factory
     * @return the service definition registry
     * @throws IllegalArgumentException if the factory class is not annotated with {@link Factory}.
     * @throws NullPointerException if the factory class is {@code null}.
     */
    ServiceDefinitionRegistry createServiceDefinitionRegistry(Object factory);
}
