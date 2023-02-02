package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * The default implementation of {@link Container}, used by {@link Container#builder()} and
 * {@link Container#create()}.
 * <p>
 * Services are instantiated lazily, when they are first requested and are cached as a singleton.
 *
 * @author Sparky983
 */
@NullMarked
final class ContainerImpl implements Container {

    private final Multimap<Class<? extends Object>, Object> singletons = new Multimap<>();

    private final ServiceDefinitionRegistry serviceDefinitionRegistry;

    ContainerImpl(final ServiceDefinitionRegistry serviceDefinitionRegistry) {

        Preconditions.requireNonNull(serviceDefinitionRegistry, "serviceDefinitionRegistry");

        this.serviceDefinitionRegistry = serviceDefinitionRegistry;
    }

    @Override
    public <T> ServiceDefinition<T> findServiceDefinition(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");

        return serviceDefinitionRegistry.findServiceDefinition(type);
    }

    @Override
    public <T> List<ServiceDefinition<T>> findServiceDefinitions(final Class<T> type) {

        Preconditions.requireNonNull(type, "type");

        return serviceDefinitionRegistry.findServiceDefinitions(type);
    }

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

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findServices(final Class<T> serviceType) {

        Preconditions.requireNonNull(serviceType, "serviceType");

        final List<T> singletonServices = (List<T>) singletons.get(serviceType);

        if (singletonServices != null) {
            return singletonServices;
        }

        try {
            final List<T> services = findServiceDefinitions(serviceType)
                    .stream()
                    .map((serviceDefinition) -> serviceDefinition.createObject(this))
                    .toList();
            synchronized (this) {
                services.forEach((service) -> singletons.add(serviceType, service));
            }
            return services;
        } catch (final NoUniqueServiceException e) {
            throw new ServiceException(e);
        }
    }

    static final class BuilderImpl implements Builder {

        private final FactoryServiceDefinitionRegistryFactory serviceDefinitionRegistryFactory
                = new FactoryServiceDefinitionRegistryFactoryImpl(
                        new ServiceServiceDefinitionFactoryImpl());

        private final ServiceDefinitionRegistry.Builder serviceDefinitions =
                ServiceDefinitionRegistry.builder();
        private final List<ServiceDefinitionRegistry> factories = new ArrayList<>();

        @Override
        public Builder serviceDefinition(
                final ServiceDefinition<? extends Object> serviceDefinition) {

            Preconditions.requireNonNull(serviceDefinition, "serviceDefinition");

            serviceDefinitions.serviceDefinition(serviceDefinition);

            return this;
        }

        @Override
        public Builder factory(final Object factory) {

            Preconditions.requireNonNull(factory, "factory");

            final ServiceDefinitionRegistry factoryRegistry = serviceDefinitionRegistryFactory
                    .createServiceDefinitionRegistry(factory);

            factories.add(factoryRegistry);

            return this;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public Builder singleton(final Object singleton) {

            Preconditions.requireNonNull(singleton, "singleton");

            singleton(singleton, (Iterable) getAllSupertypes(singleton.getClass()));

            return this;
        }

        @Override
        public <T> Builder singleton(final T singleton, final Iterable<Class<? super T>> types) {

            Preconditions.requireNonNull(singleton, "singleton");
            Preconditions.requireNonNull(types, "types");

            for (final Class<? super T> type : types) {
                serviceDefinitions.serviceDefinition(
                        new SingletonServiceDefinition<>(singleton, type));
            }

            return this;
        }

        @Override
        public <T> Builder singleton(final T singleton, final Class<? super T> type) {

            Preconditions.requireNonNull(singleton, "singleton");
            Preconditions.requireNonNull(type, "type");

            singleton(singleton, List.of(type));

            return this;
        }

        @SuppressWarnings("unchecked")
        private <T> Iterable<Class<? super T>> getAllSupertypes(
                final Class<T> type) {

            assert type != null;

            final LinkedList<Class<? super T>> queue = new LinkedList<>();

            for (Class<?> current = type; current != null; current = current.getSuperclass()) {
                queue.add((Class<? super T>) current);
                queue.addAll(Arrays.asList((Class<? super T>[]) current.getInterfaces()));
            }

            return queue;
        }

        @Override
        public Container build() {

            final List<ServiceDefinitionRegistry> serviceDefinitionRegistries =
                    new ArrayList<>(factories);
            serviceDefinitionRegistries.add(serviceDefinitions.build());

            final ServiceDefinitionRegistry serviceDefinitionRegistry =
                    new FallbackServiceDefinitionRegistry(
                            new ServiceDefinitionRegistryComposite(serviceDefinitionRegistries),
                            new InjectServiceDefinitionRegistry(
                                    new InjectServiceDefinitionFactoryImpl()
                            )
                    );

            return new ContainerImpl(serviceDefinitionRegistry);
        }
    }
}
