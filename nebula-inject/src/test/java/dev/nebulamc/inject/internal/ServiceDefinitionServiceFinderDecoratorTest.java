package dev.nebulamc.inject.internal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import dev.nebulamc.inject.NoUniqueServiceException;
import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceFinder;

class ServiceDefinitionServiceFinderDecoratorTest {

    ServiceFinder serviceFinder;
    ServiceDefinition<?> serviceDefinition;
    ServiceFinder serviceDefinitionServiceFinderDecorator;

    @BeforeEach
    void setUp() {

        serviceFinder = mock();
        serviceDefinition = mock();
        serviceDefinitionServiceFinderDecorator = new ServiceDefinitionServiceFinderDecorator(serviceFinder, serviceDefinition);
    }

    @DisplayName("<init>(ServiceFinder, ServiceDefinition<?>)")
    @Nested
    class Init {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testInitWhenServiceFinderIsNull() {

            assertThrows(NullPointerException.class, () ->
                    new ServiceDefinitionServiceFinderDecorator(null, serviceDefinition));
        }

        @SuppressWarnings("ConstantConditions")
        @Test
        void testInitWhenServiceDefinitionIsNull() {

            assertThrows(NullPointerException.class, () ->
                    new ServiceDefinitionServiceFinderDecorator(serviceFinder, null));
        }
    }

    @DisplayName("findService(Class<T>)")
    @Nested
    class FindService {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () ->
                    serviceDefinitionServiceFinderDecorator.findService(null));
        }

        @Test
        void testFindServiceWhenServiceFinderThrows() {

            when(serviceFinder.findService(Object.class)).thenThrow(NoUniqueServiceException.class);

            assertThrows(NoUniqueServiceException.class, () ->
                    serviceDefinitionServiceFinderDecorator.findService(Object.class));
        }

        @Test
        void testFindService() {

            final Object service = new Object();
            when(serviceFinder.findService(Object.class)).thenReturn(service);

            assertEquals(service, serviceDefinitionServiceFinderDecorator.findService(Object.class));
        }
    }

    @DisplayName("findOptionalService(Class<T>)")
    @Nested
    class FindOptionalService {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindOptionalServiceWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () ->
                    serviceDefinitionServiceFinderDecorator.findOptionalService(null));
        }

        @Test
        void testFindOptionalServiceWhenServiceFinderReturnsEmpty() {

            when(serviceFinder.findOptionalService(Object.class)).thenReturn(Optional.empty());

            assertEquals(
                    Optional.empty(),
                    serviceDefinitionServiceFinderDecorator.findOptionalService(Object.class));
        }

        @Test
        void testFindOptionalService() {

            final Optional<Object> service = Optional.of(new Object());
            when(serviceFinder.findOptionalService(Object.class)).thenReturn(service);

            assertEquals(
                    service,
                    serviceDefinitionServiceFinderDecorator.findOptionalService(Object.class));
        }
    }

    @DisplayName("findServices(Class<T>)")
    @Nested
    class FindServices {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServicesWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () ->
                    serviceDefinitionServiceFinderDecorator.findServices(null));
        }

        @Test
        void testFindServices() {

            final List<Object> services = List.of(new Object());
            when(serviceFinder.findServices(Object.class)).thenReturn(services);

            assertEquals(
                    services,
                    serviceDefinitionServiceFinderDecorator.findServices(Object.class));
        }
    }
}
