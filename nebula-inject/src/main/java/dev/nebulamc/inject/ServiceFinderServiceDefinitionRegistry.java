package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.util.List;

import dev.nebulamc.inject.util.Preconditions;

/**
 * A {@link ServiceDefinitionRegistry} of services found using another {@link ServiceFinder}.
 * 
 * @author Sparky983
 */
@NullMarked
final class ServiceFinderServiceDefinitionRegistry implements ServiceDefinitionRegistry {
    
    private final ServiceFinder serviceFinder;

    /**
     * Constructs a new {@link ServiceFinderServiceDefinitionRegistry} for the specified
     * {@link ServiceFinder}.
     *
     * @param serviceFinder the service finder
     * @throws NullPointerException if the serviceFinder is {@code null}.
     */
    ServiceFinderServiceDefinitionRegistry(final ServiceFinder serviceFinder) {

        Preconditions.requireNonNull(serviceFinder, "serviceFinder");

        this.serviceFinder = serviceFinder;
    }

    @Override
    public <T> ServiceDefinition<T> findServiceDefinition(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");
        
        try {
            return new SingletonServiceDefinition<>(serviceFinder.findService(type), type);
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
                            new SingletonServiceDefinition<>(service, type))
                    .toList();
        } catch (final ServiceException e) {
            return List.of();
        }
    }
}
