package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.Container;
import dev.nebulamc.inject.NoUniqueServiceException;
import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceDefinitionRegistry;
import dev.nebulamc.inject.ServiceException;
import dev.nebulamc.inject.internal.util.Multimap;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The default implementation of {@link Container}, used by {@link Container#builder()}.
 * <p>
 * Services are instantiated lazily, when they are first requested and are cached as a singleton.
 *
 * @author Sparky983
 */
@NullMarked
public final class ContainerImpl extends AbstractContainer {

    /**
     * A map of service type to service containing all currently loaded singletons.
     */
    private final Multimap<Class<?>, ?> singletons = new Multimap<>();

    private final ServiceDefinitionRegistry serviceDefinitionRegistry;

    /**
     * Constructs a new {@link ContainerImpl} using the service from the given
     * {@link ServiceDefinitionRegistry}.
     *
     * @param serviceDefinitionRegistry the service definition registry to use
     * @throws NullPointerException if {@code serviceDefinitionRegistry} is {@code null}.
     */
    public ContainerImpl(final ServiceDefinitionRegistry serviceDefinitionRegistry) {

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

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> List<T> findServices(final Class<T> serviceType) {

        Preconditions.requireNonNull(serviceType, "serviceType");

        if (serviceType.equals(Container.class)) {
            return (List<T>) List.of(this);
        }

        synchronized (singletons) {
            final List<T> singletonServices = (List<T>) singletons.get(serviceType);

            if (singletonServices != null) {
                return singletonServices;
            }

            try {
                final List<T> services = findServiceDefinitions(serviceType)
                        .stream()
                        .map((serviceDefinition) -> serviceDefinition.createService(
                                new ServiceDefinitionServiceFinderDecorator(this, serviceDefinition)))
                        .toList();
                singletons.put(serviceType, (List) services);
                return services;
            } catch (final NoUniqueServiceException e) {
                throw new ServiceException(e);
            }
        }
    }

    /**
     * The default implementation of {@link Container.Builder}, used by {@link Container#builder()}.
     */
    public static final class BuilderImpl implements Container.Builder {

        private final ParameterResolver parameterResolver = new ParameterResolverImpl();

        /**
         * The factory used by {@link #factory(Object)}.
         */
        private final FactoryServiceDefinitionRegistryFactory serviceDefinitionRegistryFactory
                = new FactoryServiceDefinitionRegistryFactoryImpl(
                new ServiceServiceDefinitionFactoryImpl(parameterResolver));

        private final ServiceDefinitionRegistry.Builder serviceDefinitions =
                ServiceDefinitionRegistry.builder();
        private final List<ServiceDefinitionRegistry> serviceDefinitionRegistries = new ArrayList<>();

        private @Nullable Container parent;

        @Override
        public Container.Builder serviceDefinition(final ServiceDefinition<?> serviceDefinition) {

            Preconditions.requireNonNull(serviceDefinition, "serviceDefinition");

            serviceDefinitions.serviceDefinition(serviceDefinition);

            return this;
        }

        @Override
        public Container.Builder serviceDefinitionRegistry(
                final ServiceDefinitionRegistry serviceDefinitionRegistry) {

            Preconditions.requireNonNull(serviceDefinitionRegistry, "serviceDefinitionRegistry");

            serviceDefinitionRegistries.add(serviceDefinitionRegistry);

            return this;
        }

        @Override
        public Container.Builder parent(final Container parent) {

            Preconditions.requireNonNull(parent, "parent");

            this.parent = parent;

            return this;
        }

        @Override
        public Container.Builder factory(final Object factory) {

            Preconditions.requireNonNull(factory, "factory");

            final ServiceDefinitionRegistry factoryRegistry = serviceDefinitionRegistryFactory
                    .createServiceDefinitionRegistry(factory);

            serviceDefinitionRegistries.add(factoryRegistry);

            return this;
        }

        @Override
        public <T> Container.Builder service(final Class<T> type, final T service) {

            Preconditions.requireNonNull(type, "type");
            Preconditions.requireNonNull(service, "singleton");

            serviceDefinitions.serviceDefinition(new SingletonServiceDefinition<>(type, service));

            return this;
        }

        @Override
        public Container build() {

            final List<ServiceDefinitionRegistry> serviceDefinitionRegistries =
                    new ArrayList<>(this.serviceDefinitionRegistries);
            serviceDefinitionRegistries.add(serviceDefinitions.build());

            if (parent != null) {
                serviceDefinitionRegistries.add(new ServiceFinderServiceDefinitionRegistry(parent));
            }

            return new ContainerImpl(
                    new FallbackServiceDefinitionRegistry(
                            new ServiceDefinitionRegistryComposite(serviceDefinitionRegistries),
                            new InjectServiceDefinitionRegistry(
                                    new InjectServiceDefinitionFactoryImpl(parameterResolver)
                            )
                    )
            );
        }
    }
}
