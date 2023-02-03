package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.util.List;

/**
 * A composite of multiple {@link ServiceDefinitionRegistry ServiceDefinitionRegistries}.
 *
 * @author Sparky983
 */
@NullMarked
final class ServiceDefinitionRegistryComposite implements ServiceDefinitionRegistry {

    private final List<ServiceDefinitionRegistry> serviceDefinitionRegistries;

    /**
     * Constructs a new {@link ServiceDefinitionRegistryComposite} from the given list of
     * {@link ServiceDefinitionRegistry ServiceDefinitionRegistries}.
     *
     * @param serviceDefinitionRegistries the
     * {@link ServiceDefinitionRegistry ServiceDefinitionRegistries} to make a composite of.
     * @throws NullPointerException if the service definition registries is or contains
     * {@code null}.
     */
    ServiceDefinitionRegistryComposite(
            final List<ServiceDefinitionRegistry> serviceDefinitionRegistries) {

        Preconditions.requireNonNull(serviceDefinitionRegistries, "serviceDefinitionRegistries");

        this.serviceDefinitionRegistries = List.copyOf(serviceDefinitionRegistries);
    }

    @Override
    public <T> ServiceDefinition<T> findServiceDefinition(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");

        return findServiceDefinitions(type)
                .stream()
                .reduce((service1, service2) -> {
                    throw new NoUniqueServiceException(
                            "Multiple service definitions for type \"" +
                                    type.getName() +
                                    "\" found");
                })
                .orElseThrow(() -> new NoUniqueServiceException(
                        "No service definition found for type \"" + type.getName() + "\""));
    }

    @Override
    public <T> List<ServiceDefinition<T>> findServiceDefinitions(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");

        return serviceDefinitionRegistries
                .stream()
                .flatMap(registry -> registry.findServiceDefinitions(type).stream())
                .toList();
    }
}
