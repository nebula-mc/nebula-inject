package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.NoUniqueServiceException;
import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceDefinitionRegistry;
import dev.nebulamc.inject.ServiceException;
import dev.nebulamc.inject.ServiceFinder;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * A {@link ServiceDefinitionRegistry} of services found using another {@link ServiceFinder}.
 *
 * @author Sparky983
 */
@NullMarked
public final class ServiceFinderServiceDefinitionRegistry implements ServiceDefinitionRegistry {

    private final ServiceFinder serviceFinder;

    /**
     * Constructs a new {@link ServiceFinderServiceDefinitionRegistry} for the specified
     * {@link ServiceFinder}.
     *
     * @param serviceFinder the service finder
     * @throws NullPointerException if the serviceFinder is {@code null}.
     */
    public ServiceFinderServiceDefinitionRegistry(final ServiceFinder serviceFinder) {

        Preconditions.requireNonNull(serviceFinder, "serviceFinder");

        this.serviceFinder = serviceFinder;
    }

    @Override
    public <T> ServiceDefinition<T> findServiceDefinition(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");

        try {
            return new SingletonServiceDefinition<>(type, serviceFinder.findService(type));
        } catch (final ServiceException e) {
            throw new NoUniqueServiceException(e);
        }
    }

    @Override
    public <T> List<ServiceDefinition<T>> findServiceDefinitions(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");

        try {
            return serviceFinder.findServices(type)
                    .stream()
                    .<ServiceDefinition<T>>map((service) ->
                            new SingletonServiceDefinition<>(type, service))
                    .toList();
        } catch (final ServiceException e) {
            return List.of();
        }
    }
}
