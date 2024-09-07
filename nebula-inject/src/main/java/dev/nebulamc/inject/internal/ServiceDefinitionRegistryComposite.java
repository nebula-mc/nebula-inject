package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceDefinitionRegistry;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * A composite of multiple {@link ServiceDefinitionRegistry ServiceDefinitionRegistries}.
 *
 * @author Sparky983
 */
@NullMarked
public final class ServiceDefinitionRegistryComposite extends AbstractServiceDefinitionRegistry {

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
    public ServiceDefinitionRegistryComposite(
            final List<ServiceDefinitionRegistry> serviceDefinitionRegistries) {

        Preconditions.requireNonNull(serviceDefinitionRegistries, "serviceDefinitionRegistries");

        this.serviceDefinitionRegistries = List.copyOf(serviceDefinitionRegistries);
    }

    @Override
    public <T> List<ServiceDefinition<T>> findServiceDefinitions(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");

        return serviceDefinitionRegistries
                .stream()
                .flatMap((registry) -> registry.findServiceDefinitions(type).stream())
                .toList();
    }
}
