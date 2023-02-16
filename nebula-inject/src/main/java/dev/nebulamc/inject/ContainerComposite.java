package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
final class ContainerComposite extends AbstractContainer {

    private final List<Container> containers;

    ContainerComposite(final List<Container> containers) {

        Preconditions.requireNonNull(containers, "containers");

        this.containers = List.copyOf(containers);
    }

    @Override
    public <T> List<ServiceDefinition<T>> findServiceDefinitions(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");

        return containers
                .stream()
                .flatMap(container -> container.findServiceDefinitions(type).stream())
                .toList();
    }

    @Override
    public <T> List<T> findServices(final Class<T> serviceType) {

        Preconditions.requireNonNull(serviceType, "serviceType");

        return containers
                .stream()
                .flatMap((container) -> container.findServices(serviceType).stream())
                .toList();
    }
}
