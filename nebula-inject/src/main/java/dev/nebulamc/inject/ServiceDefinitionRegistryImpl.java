package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.util.Collections;
import java.util.List;

/**
 * The default implementation of {@link ServiceDefinitionRegistry}, used by
 * {@link ServiceDefinitionRegistry#builder()}.
 *
 * @author Sparky983
 */
@NullMarked
final class ServiceDefinitionRegistryImpl implements ServiceDefinitionRegistry {

    private final Multimap<Class<?>, ServiceDefinition<?>> serviceDefinitions;

    /**
     * Constructs a new {@link ServiceDefinitionRegistryImpl} using the given service definitions.
     *
     * @param serviceDefinitions the service definitions to use
     * @throws NullPointerException if {@code serviceDefinitions} is {@code null}.
     */
    private ServiceDefinitionRegistryImpl(
            final Multimap<Class<?>, ServiceDefinition<?>> serviceDefinitions) {

        Preconditions.requireNonNull(serviceDefinitions, "serviceDefinitions");

        this.serviceDefinitions = new Multimap<>(serviceDefinitions);
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> List<ServiceDefinition<T>> findServiceDefinitions(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");

        // More performant than List.copyOf() and List.of() due to not requiring a copy of the list
        // And Collections.emptyList() is optimized by Collections.unmodifiableList()
        return (List) Collections.unmodifiableList(
                serviceDefinitions.getOrDefault(type, Collections.emptyList()));
    }

    /**
     * The default implementation of {@link ServiceDefinitionRegistry.Builder}, used by
     * {@link ServiceDefinitionRegistry#builder()}.
     */
    static final class BuilderImpl implements Builder {

        private final Multimap<Class<?>, ServiceDefinition<?>> serviceDefinitions =
                new Multimap<>();

        @Override
        public Builder serviceDefinition(final ServiceDefinition<?> serviceDefinition) {

            Preconditions.requireNonNull(serviceDefinition, "serviceDefinition");

            serviceDefinitions.add(serviceDefinition.getServiceType(), serviceDefinition);

            return this;
        }

        @Override
        public ServiceDefinitionRegistry build() {

            return new ServiceDefinitionRegistryImpl(serviceDefinitions);
        }
    }
}