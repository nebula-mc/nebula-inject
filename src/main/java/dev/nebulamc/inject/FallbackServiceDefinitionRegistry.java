package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.util.List;

/**
 * A service definition registry decorator that adds a fallback service definition registry.
 * <p>
 * The name is a bit misleading, as this class is a fallback registry, but adds a fallback registry
 * to an existing one.
 * <p>
 * Note that this is different to {@link ServiceDefinitionRegistryComposite} in that it only uses
 * the fallback registry if the decorated registry does not contain a service definition for the
 * requested type rather than always using it
 */
@NullMarked
final class FallbackServiceDefinitionRegistry implements ServiceDefinitionRegistry {

    private final ServiceDefinitionRegistry serviceDefinitionRegistry;
    private final ServiceDefinitionRegistry fallback;

    FallbackServiceDefinitionRegistry(final ServiceDefinitionRegistry serviceDefinitionRegistry,
                                      final ServiceDefinitionRegistry fallback) {

        Preconditions.requireNonNull(serviceDefinitionRegistry, "serviceDefinitionRegistry");
        Preconditions.requireNonNull(fallback, "fallback");

        this.serviceDefinitionRegistry = serviceDefinitionRegistry;
        this.fallback = fallback;
    }

    @Override
    public <T> ServiceDefinition<T> findServiceDefinition(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");

        try {
            return serviceDefinitionRegistry.findServiceDefinition(type);
        } catch (final NoUniqueServiceException e) {
            return fallback.findServiceDefinition(type);
        }
    }

    @Override
    public <T> List<ServiceDefinition<T>> findServiceDefinitions(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");

        final List<ServiceDefinition<T>> serviceDefinitions =
                serviceDefinitionRegistry.findServiceDefinitions(type);

        if (!serviceDefinitions.isEmpty()) {
            return serviceDefinitions;
        }

        return fallback.findServiceDefinitions(type);
    }
}
