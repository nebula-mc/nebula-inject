package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.Container;
import dev.nebulamc.inject.NoUniqueServiceException;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.nullness.NullMarked;

import java.util.List;
import java.util.Optional;

@NullMarked
public abstract class AbstractContainer extends AbstractServiceDefinitionRegistry implements Container {

    @Override
    public <T> T findService(final Class<T> serviceType) {

        Preconditions.requireNonNull(serviceType, "serviceType");

        final List<T> services = findServices(serviceType);

        if (services.isEmpty()) {
            throw new NoUniqueServiceException(
                    "No services of type \"" + serviceType.getName() + "\" found");
        }

        if (services.size() > 1) {
            throw new NoUniqueServiceException(
                    "Multiple service definitions for type \"" +
                            serviceType.getName() +
                            "\" found");
        }

        return services.get(0);
    }

    @Override
    public <T> Optional<T> findOptionalService(final Class<T> serviceType) {

        Preconditions.requireNonNull(serviceType, "serviceType");

        final List<T> services = findServices(serviceType);

        if (services.size() != 1) {
            return Optional.empty();
        }

        return Optional.of(services.get(0));
    }
}
