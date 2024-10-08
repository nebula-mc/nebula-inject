package dev.nebulamc.inject;

import org.jspecify.annotations.NullMarked;

import java.util.List;

import dev.nebulamc.inject.internal.ServiceDefinitionRegistryImpl;

/**
 * A registry of {@link ServiceDefinition ServiceDefinitions}.
 *
 * @author Sparky983
 * @since 0.1
 */
@NullMarked
public interface ServiceDefinitionRegistry {

    /**
     * Creates a new builder for a service definition registry.
     *
     * @return the builder
     * @since 0.1
     */
    static Builder builder() {

        return new ServiceDefinitionRegistryImpl.BuilderImpl();
    }

    /**
     * Finds a single implementation of the specified service type.
     *
     * @param type the service type
     * @return an unmodifiable list of all implementations of the specified service type
     * @param <T> the type of the service
     * @throws NoUniqueServiceException if zero or multiple implementations of the service type were
     * found.
     * @throws NullPointerException if the type is {@code null}.
     * @since 0.1
     */
    <T> ServiceDefinition<T> findServiceDefinition(Class<T> type);

    /**
     * Finds all implementations of the specified service type.
     *
     * @param type the service type
     * @return an unmodifiable list of all implementations of the specified service type
     * @param <T> the type of the service
     * @throws NullPointerException if the type is {@code null}.
     * @since 0.1
     */
    <T> List<ServiceDefinition<T>> findServiceDefinitions(Class<T> type);


    /**
     * A builder for a service definition registry.
     *
     * @see #builder()
     * @since 0.1
     */
    interface Builder {

        /**
         * Adds a service definition to the service definition registry.
         *
         * @param serviceDefinition the service definition
         * @return this builder (for chaining)
         * @throws NullPointerException if the service definition is {@code null}.
         * @since 0.1
         */
        Builder serviceDefinition(ServiceDefinition<?> serviceDefinition);

        /**
         * Adds a service definition registry to the service definition registry.
         *
         * @param serviceDefinitionRegistry the service definition registry
         * @return this builder (for chaining)
         * @throws NullPointerException if the service definition registry is {@code null}.
         * @since 0.3
         */
        Builder serviceDefinitionRegistry(ServiceDefinitionRegistry serviceDefinitionRegistry);

        /**
         * Builds the service definition registry.
         *
         * @return the built service definition registry.
         * @since 0.1
         */
        ServiceDefinitionRegistry build();
    }
}
