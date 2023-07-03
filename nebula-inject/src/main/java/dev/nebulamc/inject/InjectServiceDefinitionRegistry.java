package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.util.List;

import dev.nebulamc.inject.util.Preconditions;

/**
 * A {@link ServiceDefinitionRegistry} for {@link InjectServiceDefinition ServiceDefinitions} of
 * concrete classes.
 *
 * @author Sparky983
 */
@NullMarked
final class InjectServiceDefinitionRegistry implements ServiceDefinitionRegistry {

    private final InjectServiceDefinitionFactory serviceDefinitionFactory;

    InjectServiceDefinitionRegistry(final InjectServiceDefinitionFactory serviceDefinitionFactory) {

        Preconditions.requireNonNull(serviceDefinitionFactory, "serviceDefinitionFactory");

        this.serviceDefinitionFactory = serviceDefinitionFactory;
    }

    @Override
    public <T> ServiceDefinition<T> findServiceDefinition(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");

        final List<ServiceDefinition<T>> serviceDefinitions = findServiceDefinitions(type);

        assert serviceDefinitions.size() <= 1;

        if (serviceDefinitions.isEmpty()) {
            throw new NoUniqueServiceException("Unable to find service definition of type \"" +
                    type.getName() +
                    "\"" +
                    ". Probable cause: type has no injectable constructor.");
        }

        return serviceDefinitions.get(0);
    }

    @Override
    public <T> List<ServiceDefinition<T>> findServiceDefinitions(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");

        try {
            return List.of(serviceDefinitionFactory.createServiceDefinition(type, type));
        } catch (final IllegalArgumentException e) {
            return List.of();
        }
    }
}
