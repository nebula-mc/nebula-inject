package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

@NullMarked
final class SingletonServiceDefinition<T> implements ServiceDefinition<T> {

    private final T singleton;
    private final Class<T> serviceType;

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
    public T createObject(final ServiceFinder serviceFinder) {

        Preconditions.requireNonNull(serviceFinder, "serviceFinder");

        return singleton;
    }
}
