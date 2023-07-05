package dev.nebulamc.inject;

import dev.nebulamc.inject.util.Preconditions;
import org.jspecify.nullness.NullMarked;

/**
 * An abstract {@link ServiceDefinitionRegistry} implementation that requires subclasses to only
 * implement an {@link #findServiceDefinitions(Class)} method.
 *
 * @author Sparky983
 */
@NullMarked
abstract class AbstractServiceDefinitionRegistry implements ServiceDefinitionRegistry {

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
