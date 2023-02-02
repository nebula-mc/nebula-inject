package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

/**
 * A factory for creating {@link ServiceDefinitionRegistry} objects for {@link Factory} classes.
 *
 * @author Sparky983
 */
@NullMarked
interface FactoryServiceDefinitionRegistryFactory {

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
