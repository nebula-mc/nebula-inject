package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;
import dev.nebulamc.inject.internal.ContainerImpl;

/**
 * Represents the dependency injection container.
 *
 * @author Sparky983
 * @see #builder()
 * @since 0.1
 */
@NullMarked
public interface Container extends ServiceFinder, ServiceDefinitionRegistry {

    /**
     * Creates a new builder for the dependency injection container.
     *
     * @return the builder
     * @since 0.1
     */
    static Builder builder() {

        return new ContainerImpl.BuilderImpl();
    }

    /**
     * A builder for the dependency injection container.
     *
     * @see #builder()
     * @since 0.1
     */
    interface Builder extends ServiceDefinitionRegistry.Builder {

        /**
         * Sets the parent of this container.
         *
         * @param parent the parent container
         * @return this builder (for chaining)
         * @throws NullPointerException if the parent is {@code null}.
         * @since 0.1
         */
        Builder parent(Container parent);

        /**
         * Adds a factory to the container.
         *
         * @param factory the factory instance
         * @return this builder (for chaining)
         * @throws IllegalArgumentException if the factory class is not annotated with
         * {@link Factory @Factory}.
         * @throws NullPointerException if the factory is {@code null}.
         * @see Factory
         * @see Service
         * @since 0.1
         */
        Builder factory(Object factory);

        /**
         * Adds a singleton to the container for all super types of the singleton's type (including
         * the singleton's type itself).
         *
         * @param singleton the singleton instance
         * @return this builder (for chaining)
         * @throws NullPointerException if the singleton is {@code null}.
         * @since 0.1
         */
        Builder singleton(Object singleton);

        /**
         * Adds a singleton to the container for the specified types
         *
         * @param types the types to register the singleton as
         * @param singleton the singleton instance
         * @return this builder (for chaining)
         * @param <T> the type of the singleton
         * @throws NullPointerException if the singleton is {@code null} is the types are or
         * contains {@code null}.
         * @since 0.1
         */
        <T> Builder singleton(Iterable<Class<? super T>> types, T singleton);

        /**
         * Adds a singleton to the container for the specified types
         *
         * @param type the type to register the singleton as
         * @param singleton the singleton instance
         * @return this builder (for chaining)
         * @param <T> the type of the singleton
         * @throws NullPointerException if the singleton or type are {@code null}.
         * @since 0.1
         */
        <T> Builder singleton(Class<? super T> type, T singleton);

        /**
         * Adds a service definition to the container.
         *
         * @param serviceDefinition the service definition
         * @return this builder (for chaining)
         * @throws NullPointerException if the service definition is {@code null}.
         * @since 0.1
         */
        @Override
        Builder serviceDefinition(ServiceDefinition<?> serviceDefinition);

        /**
         * Adds a service definition registry to the container.
         *
         * @param serviceDefinitionRegistry the service definition registry
         * @return this builder (for chaining)
         * @throws NullPointerException if the service definition registry is {@code null}.
         * @since 0.3
         */
        @Override
        Builder serviceDefinitionRegistry(ServiceDefinitionRegistry serviceDefinitionRegistry);

        /**
         * Builds the container.
         *
         * @return the built container
         * @since 0.1
         */
        @Override
        Container build();
    }
}
