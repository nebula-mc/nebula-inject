package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

/**
 * Represents the dependency injection container.
 *
 * @author Sparky983
 * @see #create()
 * @since 1.0
 */
@NullMarked
public interface Container extends ServiceFinder, ServiceDefinitionRegistry {

    /**
     * Creates a new builder for the dependency injection container.
     *
     * @return the builder
     * @since 1.0
     */
    static Builder builder() {

        return new ContainerImpl.BuilderImpl();
    }

    /**
     * Creates a new dependency injection container.
     *
     * @return the container
     * @since 1.0
     */
    static Container create() {

        return builder().build();
    }

    /**
     * A builder for the dependency injection container.
     *
     * @see #builder()
     * @since 1.0
     */
    interface Builder extends ServiceDefinitionRegistry.Builder {

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
         * @since 1.0
         */
        Builder factory(Object factory);

        /**
         * Adds a singleton to the container for all super types of the singleton's type (including
         * the singleton's type itself).
         *
         * @param singleton the singleton instance
         * @return this builder (for chaining)
         * @throws NullPointerException if the singleton is {@code null}.
         * @since 1.0
         */
        Builder singleton(Object singleton);

        /**
         * Adds a singleton to the container for the specified types
         *
         * @param singleton the singleton instance
         * @param types the types to register the singleton as
         * @return this builder (for chaining)
         * @param <T> the type of the singleton
         * @throws NullPointerException if the singleton is {@code null} is the types are or
         * contains {@code null}.
         * @since 1.0
         */
        <T> Builder singleton(T singleton, Iterable<Class<? super T>> types);

        /**
         * Adds a singleton to the container for the specified types
         *
         * @param singleton the singleton instance
         * @param type the type to register the singleton as
         * @return this builder (for chaining)
         * @param <T> the type of the singleton
         * @throws NullPointerException if the singleton or type are {@code null}.
         * @since 1.0
         */
        <T> Builder singleton(T singleton, Class<? super T> type);

        /**
         * Adds a service definition to the container.
         *
         * @param serviceDefinition the service definition
         * @return this builder (for chaining)
         * @throws NullPointerException if the factory is {@code null}.
         * @since 1.0
         */
        @Override
        Builder serviceDefinition(ServiceDefinition<? extends Object> serviceDefinition);

        /**
         * Builds the container.
         *
         * @return the built container
         * @since 1.0
         */
        @Override
        Container build();
    }
}
