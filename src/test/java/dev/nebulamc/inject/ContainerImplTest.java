package dev.nebulamc.inject;

import dev.nebulamc.inject.car.Car;
import dev.nebulamc.inject.car.Engine;
import dev.nebulamc.inject.car.V8Engine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ContainerImplTest {

    ServiceDefinitionRegistry serviceDefinitionRegistry;
    Container container;

    @BeforeEach
    void setUp() {

        serviceDefinitionRegistry = mock();
        container = new ContainerImpl(serviceDefinitionRegistry);
    }

    @AfterEach
    void tearDown() {

        verifyNoMoreInteractions(serviceDefinitionRegistry);
    }

    @DisplayName("findServiceDefinition(Class<T>)")
    @Nested
    class FindServiceDefinition {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceDefinitionWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () -> container.findServiceDefinition(null));
        }

        @Test
        void testFindServiceDefinitionWhenNotFound() {

            when(serviceDefinitionRegistry.findServiceDefinition(Car.class))
                    .thenThrow(NoUniqueServiceException.class);

            assertThrows(NoUniqueServiceException.class, () ->
                    serviceDefinitionRegistry.findServiceDefinition(Car.class));
            verify(serviceDefinitionRegistry).findServiceDefinition(Car.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceDefinition() {

            final ServiceDefinition<Car> serviceDefinition = mock();
            when(serviceDefinitionRegistry.findServiceDefinition(Car.class))
                    .thenReturn(serviceDefinition);

            final ServiceDefinition<Car> result = container.findServiceDefinition(Car.class);

            assertEquals(serviceDefinition, result);
            verify(serviceDefinitionRegistry).findServiceDefinition(Car.class);
            verifyNoMoreInteractions(serviceDefinition);
        }
    }

    @DisplayName("findServiceDefinitions(Class<T>)")
    @Nested
    class FindServiceDefinitions {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceDefinitionsWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () -> container.findServiceDefinitions(null));
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceDefinitions() {

            final ServiceDefinition<Car> serviceDefinition = mock();
            when(serviceDefinitionRegistry.findServiceDefinitions(Car.class))
                    .thenReturn(List.of(serviceDefinition));

            final List<ServiceDefinition<Car>> result = container.findServiceDefinitions(Car.class);

            assertEquals(List.of(serviceDefinition), result);
            verify(serviceDefinitionRegistry).findServiceDefinitions(Car.class);
            verifyNoMoreInteractions(serviceDefinition);
        }
    }

    @DisplayName("findService(Class<T>)")
    @Nested
    class FindService {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () -> container.findService(null));
        }

        @Test
        void testFindServiceWhenServiceNotFound() {

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of());

            assertThrows(NoUniqueServiceException.class, () -> container.findService(Engine.class));

            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceWhenMultipleServicesFound() {

            final ServiceDefinition<Engine> serviceDefinition = mock();
            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition, serviceDefinition));

            assertThrows(NoUniqueServiceException.class, () -> container.findService(Engine.class));

            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceWhenRequiredServiceNotFound() {

            final ServiceDefinition<Engine> serviceDefinition = mock();
            when(serviceDefinition.createObject(container))
                    .thenThrow(NoUniqueServiceException.class);

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));

            assertThrows(ServiceException.class,
                    () -> container.findService(Engine.class));
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceWhenServiceException() {

            final ServiceDefinition<Engine> serviceDefinition = mock();
            when(serviceDefinition.createObject(container)).thenThrow(ServiceException.class);

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));

            assertThrows(ServiceException.class, () -> container.findService(Engine.class));
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
            verify(serviceDefinition).createObject(container);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindService() {

            final ServiceDefinition<Engine> serviceDefinition = mock();
            when(serviceDefinition.createObject(container)).thenReturn(new V8Engine());

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));

            final Engine engine = container.findService(Engine.class);

            container.findService(Engine.class);
            // Make sure dependencies aren't called because it should be cached

            assertInstanceOf(V8Engine.class, engine);
            verify(serviceDefinition).createObject(container);
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
            verifyNoMoreInteractions(serviceDefinition);
        }
    }

    @DisplayName("findOptionalService(Class<T>)")
    @Nested
    class FindOptionalService {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindOptionalServiceWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () -> container.findOptionalService(null));
        }

        @Test
        void testFindOptionalServiceWhenServiceNotFound() {

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of());

            final Optional<Engine> engine = container.findOptionalService(Engine.class);

            assertEquals(Optional.empty(), engine);
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindOptionalServiceWhenRequiredServiceNotFound() {

            final ServiceDefinition<Engine> serviceDefinition = mock();
            when(serviceDefinition.createObject(container))
                    .thenThrow(NoUniqueServiceException.class);

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));

            assertThrows(ServiceException.class,
                    () -> container.findOptionalService(Engine.class));
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindOptionalServiceWhenServiceException() {

            final ServiceDefinition<Engine> serviceDefinition = mock();
            when(serviceDefinition.createObject(container)).thenThrow(ServiceException.class);

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));

            assertThrows(ServiceException.class, () -> container.findOptionalService(Engine.class));
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
            verify(serviceDefinition).createObject(container);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindOptionalService() {

            final ServiceDefinition<Engine> serviceDefinition = mock();
            when(serviceDefinition.createObject(container)).thenReturn(new V8Engine());

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));

            final Optional<Engine> engine = container.findOptionalService(Engine.class);

            container.findOptionalService(Engine.class);
            // Make sure dependencies aren't called because it should be cached

            assertTrue(engine.isPresent());
            assertInstanceOf(V8Engine.class, engine.get());
            verify(serviceDefinition).createObject(container);
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
            verifyNoMoreInteractions(serviceDefinition);
        }
    }

    @DisplayName("findServices(Class<T>)")
    @Nested
    class FindServices {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServicesWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () -> container.findServices(null));
        }

        @Test
        void testFindServicesWhenServiceNotFound() {

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of());

            final List<Engine> engine = container.findServices(Engine.class);

            assertEquals(List.of(), engine);
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServicesWhenRequiredServiceNotFound() {

            final ServiceDefinition<Engine> serviceDefinition = mock();
            when(serviceDefinition.createObject(container))
                    .thenThrow(NoUniqueServiceException.class);

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));

            assertThrows(ServiceException.class,
                    () -> container.findServices(Engine.class));
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServicesWhenServiceException() {

            final ServiceDefinition<Engine> serviceDefinition = mock();
            when(serviceDefinition.createObject(container)).thenThrow(ServiceException.class);

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));

            assertThrows(ServiceException.class, () -> container.findServices(Engine.class));
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
            verify(serviceDefinition).createObject(container);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServices() {

            final ServiceDefinition<Engine> serviceDefinition = mock();
            when(serviceDefinition.createObject(container)).thenReturn(new V8Engine());

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));

            final List<Engine> engine = container.findServices(Engine.class);

            container.findServices(Engine.class);
            // Make sure dependencies aren't called because it should be cached

            assertEquals(1, engine.size());
            assertInstanceOf(V8Engine.class, engine.get(0));
            verify(serviceDefinition).createObject(container);
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
            verifyNoMoreInteractions(serviceDefinition);
        }
    }
}
