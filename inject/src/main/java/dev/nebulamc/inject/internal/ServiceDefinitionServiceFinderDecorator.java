package dev.nebulamc.inject.internal;

import org.jspecify.nullness.NullMarked;

import java.util.List;
import java.util.Optional;

import dev.nebulamc.inject.NoUniqueServiceException;
import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceFinder;
import dev.nebulamc.inject.internal.util.Preconditions;

/**
 * Decorates a {@link ServiceFinder} by making exception messages specific to a
 * {@link ServiceDefinition}.
 *
 * @author Sparky983
 */
@NullMarked
final class ServiceDefinitionServiceFinderDecorator implements ServiceFinder {

    private final ServiceFinder serviceFinder;
    private final ServiceDefinition<?> serviceDefinition;

    ServiceDefinitionServiceFinderDecorator(final ServiceFinder serviceFinder,
                                            final ServiceDefinition<?> serviceDefinition) {

        Preconditions.requireNonNull(serviceFinder, "serviceFinder");
        Preconditions.requireNonNull(serviceDefinition, "serviceDefinition");

        this.serviceFinder = serviceFinder;
        this.serviceDefinition = serviceDefinition;
    }

    @Override
    public <T> T findService(final Class<T> serviceType) {

        Preconditions.requireNonNull(serviceType, "serviceType");

        try {
            return serviceFinder.findService(serviceType);
        } catch (final NoUniqueServiceException e) {
            throw new NoUniqueServiceException(
                    "Service of type " + serviceDefinition.getServiceType() +
                            " required a service of type " + serviceType +
                            " but there were either none or multiple", e);
        }
    }

    @Override
    public <T> Optional<T> findOptionalService(final Class<T> serviceType) {

        Preconditions.requireNonNull(serviceType, "serviceType");

        return serviceFinder.findOptionalService(serviceType);
    }

    @Override
    public <T> List<T> findServices(final Class<T> serviceType) {

        Preconditions.requireNonNull(serviceType, "serviceType");

        return serviceFinder.findServices(serviceType);
    }
}
