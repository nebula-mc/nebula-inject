package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.NoUniqueServiceException;
import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceDefinitionRegistry;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.annotations.NullMarked;

/**
 * An abstract {@link ServiceDefinitionRegistry} implementation that requires subclasses to only
 * implement an {@link #findServiceDefinitions(Class)} method.
 *
 * @author Sparky983
 */
@NullMarked
public abstract class AbstractServiceDefinitionRegistry implements ServiceDefinitionRegistry {

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
}
