package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import dev.nebulamc.inject.util.Preconditions;

/**
 * A service definition for a singleton object.
 *
 * @author Sparky983
 * @param <T> the service type
 * @see ContainerImpl.BuilderImpl#singleton(Object)
 */
@NullMarked
final class SingletonServiceDefinition<T> implements ServiceDefinition<T> {

    private final T singleton;
    private final Class<T> serviceType;

    /**
     * Constructs a new {@link SingletonServiceDefinition} for the specified singleton object.
     *
     * @param singleton the singleton object
     * @param serviceType the service type
     * @throws NullPointerException if {@code singleton} or {@code serviceType} are {@code null}.
     */
    SingletonServiceDefinition(final T singleton, final Class<T> serviceType) {

        Preconditions.requireNonNull(singleton, "singleton");
        Preconditions.requireNonNull(serviceType, "type");

        this.singleton = singleton;
        this.serviceType = serviceType;
    }

    @Override
    public Class<T> getServiceType() {

        return serviceType;
    }

    @Override
    public T createService(final ServiceFinder serviceFinder) {

        Preconditions.requireNonNull(serviceFinder, "serviceFinder");

        return singleton;
    }
}
