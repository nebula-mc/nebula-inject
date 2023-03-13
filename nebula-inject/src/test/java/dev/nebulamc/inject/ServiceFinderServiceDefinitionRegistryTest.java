package dev.nebulamc.inject;

import dev.nebulamc.inject.car.Wheels;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ServiceFinderServiceDefinitionRegistryTest {

    ServiceFinder serviceFinder;
    ServiceFinder unusedServiceFinder;
    ServiceDefinitionRegistry serviceDefinitionRegistry;

    @BeforeEach
    void setUp() {

        serviceFinder = mock();
        unusedServiceFinder = mock();
        serviceDefinitionRegistry = new ServiceFinderServiceDefinitionRegistry(serviceFinder);
    }

    @AfterEach
    void tearDown() {

        verifyNoMoreInteractions(serviceFinder);
        verifyNoInteractions(unusedServiceFinder);
    }

    @DisplayName("<init>(ServiceFinder)")
    @Nested
    class Init {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testInitThrowsWhenServiceFinderIsNull() {

            assertThrows(NullPointerException.class, () ->
                    new ServiceFinderServiceDefinitionRegistry(null));
        }
    }

    @DisplayName("findServiceDefinition(Class<T>)")
    @Nested
    class FindServiceDefinition {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceDefinitionThrowsWhenServiceTypeIsNull() {

            assertThrows(NullPointerException.class, () ->
                    serviceDefinitionRegistry.findServiceDefinition(null));
        }

        @Test
        void testFindServiceDefinitionWhenServiceIsNotFound() {

            when(serviceFinder.findService(Wheels.class))
                    .thenThrow(NoUniqueServiceException.class);

            assertThrows(NoUniqueServiceException.class, () ->
                    serviceDefinitionRegistry.findServiceDefinition(Wheels.class));
            verify(serviceFinder).findService(Wheels.class);
        }

        @Test
        void testFindServiceDefinitionWhenExceptionIsThrown() {

            when(serviceFinder.findService(Wheels.class))
                    .thenThrow(ServiceException.class);

            assertThrows(NoUniqueServiceException.class, () ->
                    serviceDefinitionRegistry.findServiceDefinition(Wheels.class));
            verify(serviceFinder).findService(Wheels.class);
        }

        @Test
        void testFindServiceDefinition() {

            final Wheels wheels = new Wheels();
            when(serviceFinder.findService(Wheels.class)).thenReturn(wheels);

            final ServiceDefinition<Wheels> serviceDefinition = serviceDefinitionRegistry
                    .findServiceDefinition(Wheels.class);

            assertEquals(Wheels.class, serviceDefinition.getServiceType());
            assertEquals(wheels, serviceDefinition.createService(unusedServiceFinder));
            verify(serviceFinder).findService(Wheels.class);
        }
    }

    @DisplayName("findServiceDefinitions(Class<T>)")
    @Nested
    class FindServiceDefinitions {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceDefinitionsThrowsWhenServiceTypeIsNull() {

            assertThrows(NullPointerException.class, () ->
                    serviceDefinitionRegistry.findServiceDefinitions(null));
        }

        @Test
        void testFindServiceDefinitionsWhenExceptionIsThrown() {

            when(serviceFinder.findServices(Wheels.class))
                    .thenThrow(ServiceException.class);

            assertEquals(List.of(), serviceDefinitionRegistry.findServiceDefinitions(Wheels.class));
            verify(serviceFinder).findServices(Wheels.class);
        }

        @Test
        void testFindServiceDefinitions() {

            final Wheels wheels1 = new Wheels();
            final Wheels wheels2 = new Wheels();
            when(serviceFinder.findServices(Wheels.class)).thenReturn(List.of(wheels1, wheels2));

            final List<ServiceDefinition<Wheels>> serviceDefinitions = serviceDefinitionRegistry
                    .findServiceDefinitions(Wheels.class);

            assertEquals(2, serviceDefinitions.size());
            assertEquals(Wheels.class, serviceDefinitions.get(0).getServiceType());
            assertEquals(wheels1, serviceDefinitions.get(0).createService(unusedServiceFinder));
            assertEquals(Wheels.class, serviceDefinitions.get(1).getServiceType());
            assertEquals(wheels2, serviceDefinitions.get(1).createService(unusedServiceFinder));
            verify(serviceFinder).findServices(Wheels.class);
        }
    }
}
