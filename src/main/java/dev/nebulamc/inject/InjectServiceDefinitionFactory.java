package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

/**
 * A factory for creating {@link ServiceDefinition}s for classes with
 * <a href="package-summary.html#injectable-constructors">injectable constructors</a>.
 *
 * @author Sparky983
 */
@NullMarked
interface InjectServiceDefinitionFactory {

    /**
     * Creates a {@link ServiceDefinition} for the given service type and implementation.
     *
     * @param type the type of the service
     * @param implementation the implementation of the service
     * @param <T> the type of the service
     * @return the service definition
     * @throws IllegalArgumentException if the implementation does not have an
     * <a href="package-summary.html#injectable-constructors">injectable constructor</a>.
     * @throws NullPointerException if the service type or implementation classes are {@code null}.
     */
    <T> ServiceDefinition<T> createServiceDefinition(Class<T> type,
                                                     Class<? extends T> implementation);
}
