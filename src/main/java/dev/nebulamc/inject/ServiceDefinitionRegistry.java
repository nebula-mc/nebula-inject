package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.util.List;

/**
 * A registry of {@link ServiceDefinition ServiceDefinitions}.
 *
 * @author Sparky983
 * @since 1.0
 */
@NullMarked
public interface ServiceDefinitionRegistry {

    /**
     * Finds a single implementation of the specified service type.
     *
     * @param type the service type
     * @return an unmodifiable list of all implementations of the specified service type
     * @param <T> the type of the service
     * @throws NoUniqueServiceException if zero or multiple implementations of the service type were
     * found.
     * @throws NullPointerException if the type is {@code null}.
     * @since 1.0
     */
    <T> ServiceDefinition<T> findServiceDefinition(Class<T> type);

    /**
     * Finds all implementations of the specified service type.
     *
     * @param type the service type
     * @return an unmodifiable list of all implementations of the specified service type
     * @param <T> the type of the service
     * @throws NullPointerException if the type is {@code null}.
     * @since 1.0
     */
    <T> List<ServiceDefinition<T>> findServiceDefinitions(Class<T> type);

    /**
     * Creates a new builder for a service definition registry.
     *
     * @return the builder
     * @since 1.0
     */
    static Builder builder() {

        return new ServiceDefinitionRegistryImpl.BuilderImpl();
    }


    /**
     * A builder for a service definition registry.
     *
     * @see #builder()
     * @since 1.0
     */
    interface Builder {

        /**
         * Adds a service definition to the service definition registry.
         *
         * @param serviceDefinition the service definition
         * @return this builder (for chaining)
         * @throws NullPointerException if the factory is {@code null}.
         * @since 1.0
         */
        Builder serviceDefinition(ServiceDefinition<? extends Object> serviceDefinition);

        /**
         * Builds the service definition registry.
         *
         * @return the built service definition registry.
         * @since 1.0
         */
        ServiceDefinitionRegistry build();
    }
}
