package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.util.Collections;
import java.util.List;

@NullMarked
final class ServiceDefinitionRegistryImpl implements ServiceDefinitionRegistry {

    private final Multimap<Class<?>, ServiceDefinition<?>> serviceDefinitions;

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
