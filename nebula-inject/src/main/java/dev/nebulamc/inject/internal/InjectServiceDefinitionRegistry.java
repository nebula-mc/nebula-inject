package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.NoUniqueServiceException;
import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceDefinitionRegistry;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * A {@link ServiceDefinitionRegistry} for {@link InjectServiceDefinition ServiceDefinitions} of
 * concrete classes.
 *
 * @author Sparky983
 */
@NullMarked
public final class InjectServiceDefinitionRegistry implements ServiceDefinitionRegistry {

    private final InjectServiceDefinitionFactory serviceDefinitionFactory;

    public InjectServiceDefinitionRegistry(final InjectServiceDefinitionFactory serviceDefinitionFactory) {

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
