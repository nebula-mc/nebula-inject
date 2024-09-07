package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceFinder;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.annotations.NullMarked;

/**
 * A service definition for a singleton object.
 *
 * @author Sparky983
 * @param <T> the service type
 * @see ContainerImpl.BuilderImpl#singleton(Object)
 */
@NullMarked
final class SingletonServiceDefinition<T> implements ServiceDefinition<T> {

    private final Class<T> serviceType;
    private final T singleton;

    /**
     * Constructs a new {@link SingletonServiceDefinition} for the specified singleton object.
     *
     * @param singleton the singleton object
     * @param serviceType the service type
     * @throws NullPointerException if {@code singleton} or {@code serviceType} are {@code null}.
     */
    SingletonServiceDefinition(final Class<T> serviceType, final T singleton) {

        Preconditions.requireNonNull(serviceType, "type");
        Preconditions.requireNonNull(singleton, "singleton");

        this.serviceType = serviceType;
        this.singleton = singleton;
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
