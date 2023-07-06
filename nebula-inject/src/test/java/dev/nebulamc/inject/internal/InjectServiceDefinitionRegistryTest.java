package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.NoUniqueServiceException;
import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceDefinitionRegistry;
import dev.nebulamc.inject.car.Car;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class InjectServiceDefinitionRegistryTest {

    InjectServiceDefinitionFactory serviceDefinitionFactory;
    ServiceDefinitionRegistry registry;

    @BeforeEach
    void setUp() {

        serviceDefinitionFactory = mock();
        registry = new InjectServiceDefinitionRegistry(serviceDefinitionFactory);
    }

    @AfterEach
    void tearDown() {

        verifyNoMoreInteractions(serviceDefinitionFactory);
    }

    @DisplayName("<init>(InjectServiceDefinitionFactory)")
    @Nested
    class Init {

        @Test
        void testInitWhenServiceDefinitionFactoryIsNull() {

            assertThrows(NullPointerException.class, () -> new InjectServiceDefinitionRegistry(null));
        }
    }

    @DisplayName("findServiceDefinition(Class<T>)")
    @Nested
    class FindServiceDefinition {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceDefinitionWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () -> registry.findServiceDefinition(null));
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceDefinitionWhenTypeIsInjectable() {

            final ServiceDefinition<Car> serviceDefinition = mock();
            when(serviceDefinitionFactory.createServiceDefinition(Car.class, Car.class))
                    .thenReturn(serviceDefinition);

            final ServiceDefinition<Car> result = registry.findServiceDefinition(Car.class);

            assertEquals(serviceDefinition, result);

            verify(serviceDefinitionFactory).createServiceDefinition(Car.class, Car.class);
            verifyNoMoreInteractions(serviceDefinition);
        }

        @Test
        void testFindServiceDefinitionWhenTypeIsNotInjectable() {

            when(serviceDefinitionFactory.createServiceDefinition(Car.class, Car.class))
                    .thenThrow(IllegalArgumentException.class);

            assertThrows(NoUniqueServiceException.class, () ->
                    registry.findServiceDefinition(Car.class));
            verify(serviceDefinitionFactory).createServiceDefinition(Car.class, Car.class);
        }
    }

    @DisplayName("findServiceDefinitions(Class<T>)")
    @Nested
    class FindServiceDefinitions {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceDefinitionsWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () -> registry.findServiceDefinitions(null));
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceDefinitionsWhenTypeIsInjectable() {

            final ServiceDefinition<Car> serviceDefinition = mock();
            when(serviceDefinitionFactory.createServiceDefinition(Car.class, Car.class))
                    .thenReturn(serviceDefinition);

            final List<ServiceDefinition<Car>> result = registry.findServiceDefinitions(Car.class);

            assertEquals(List.of(serviceDefinition), result);

            verify(serviceDefinitionFactory).createServiceDefinition(Car.class, Car.class);
            verifyNoMoreInteractions(serviceDefinition);
        }

        @Test
        void testFindServiceDefinitionsWhenTypeIsNotInjectable() {

            when(serviceDefinitionFactory.createServiceDefinition(Car.class, Car.class))
                    .thenThrow(IllegalArgumentException.class);

            final List<ServiceDefinition<Car>> serviceDefinitions =
                    registry.findServiceDefinitions(Car.class);

            assertEquals(List.of(), serviceDefinitions);
            verify(serviceDefinitionFactory).createServiceDefinition(Car.class, Car.class);
        }
    }
}
