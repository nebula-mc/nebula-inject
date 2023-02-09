package dev.nebulamc.inject;

import dev.nebulamc.inject.car.Engine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class FallbackServiceDefinitionRegistryTest {

    ServiceDefinitionRegistry serviceDefinitionRegistry;
    ServiceDefinitionRegistry fallback;
    /**
     * The {@link FallbackServiceDefinitionRegistry}.
     */
    ServiceDefinitionRegistry fallbackServiceDefinitionRegistry;

    @BeforeEach
    void setUp() {

        serviceDefinitionRegistry = mock();
        fallback = mock();
        fallbackServiceDefinitionRegistry = new FallbackServiceDefinitionRegistry(
                serviceDefinitionRegistry, fallback);
    }

    @AfterEach
    void tearDown() {

        verifyNoMoreInteractions(serviceDefinitionRegistry, fallback);
    }

    @DisplayName("<init>(ServiceDefinitionRegistry, ServiceDefinitionRegistry)")
    @Nested
    class Init {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testInitWhenServiceDefinitionRegistryIsNull() {

            assertThrows(NullPointerException.class,
                    () -> new FallbackServiceDefinitionRegistry(null, fallback));
        }

        @SuppressWarnings("ConstantConditions")
        @Test
        void testInitWhenFallbackIsNull() {

            assertThrows(NullPointerException.class,
                    () -> new FallbackServiceDefinitionRegistry(serviceDefinitionRegistry, null));
        }
    }

    @DisplayName("findServiceDefinition(Class<T>)")
    @Nested
    class FindServiceDefinition {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceDefinitionWhenTypeIsNull() {

            assertThrows(NullPointerException.class,
                    () -> fallbackServiceDefinitionRegistry.findServiceDefinition(null));
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceDefinitionWhenServiceDefinitionRegistryHasService() {

            final ServiceDefinition<Engine> serviceDefinition = mock();
            when(serviceDefinitionRegistry.findServiceDefinition(Engine.class))
                    .thenReturn(serviceDefinition);

            final ServiceDefinition<Engine> result = fallbackServiceDefinitionRegistry
                    .findServiceDefinition(Engine.class);

            assertEquals(serviceDefinition, result);
            verify(serviceDefinitionRegistry).findServiceDefinition(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceDefinitionWhenServiceDefinitionRegistryDoesNotHaveService() {

            final ServiceDefinition<Engine> serviceDefinition = mock();

            when(serviceDefinitionRegistry.findServiceDefinition(Engine.class))
                    .thenThrow(NoUniqueServiceException.class);
            when(fallback.findServiceDefinition(Engine.class))
                    .thenReturn(serviceDefinition);

            final ServiceDefinition<Engine> result = fallbackServiceDefinitionRegistry
                    .findServiceDefinition(Engine.class);

            assertEquals(serviceDefinition, result);
            verify(serviceDefinitionRegistry).findServiceDefinition(Engine.class);
            verify(fallback).findServiceDefinition(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceDefinitionWhenServiceDefinitionRegistryAndFallbackDoesNotHaveService() {

            when(serviceDefinitionRegistry.findServiceDefinition(Engine.class))
                    .thenThrow(NoUniqueServiceException.class);
            when(fallback.findServiceDefinition(Engine.class))
                    .thenThrow(NoUniqueServiceException.class);

            assertThrows(NoUniqueServiceException.class,
                    () -> fallbackServiceDefinitionRegistry.findServiceDefinition(Engine.class));
            verify(serviceDefinitionRegistry).findServiceDefinition(Engine.class);
            verify(fallback).findServiceDefinition(Engine.class);
        }
    }

    @DisplayName("findServiceDefinitions(Class<T>)")
    @Nested
    class FindServiceDefinitions {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceDefinitionsWhenTypeIsNull() {

            assertThrows(NullPointerException.class,
                    () -> fallbackServiceDefinitionRegistry.findServiceDefinitions(null));
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceDefinitionsWhenServiceDefinitionRegistryHasService() {

            final ServiceDefinition<Engine> serviceDefinition = mock();
            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));

            final List<ServiceDefinition<Engine>> result = fallbackServiceDefinitionRegistry
                    .findServiceDefinitions(Engine.class);

            assertEquals(List.of(serviceDefinition), result);
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceDefinitionsWhenServiceDefinitionRegistryDoesNotHaveService() {

            final ServiceDefinition<Engine> serviceDefinition = mock();

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of());
            when(fallback.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));

            final List<ServiceDefinition<Engine>> result = fallbackServiceDefinitionRegistry
                    .findServiceDefinitions(Engine.class);

            assertEquals(List.of(serviceDefinition), result);
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
            verify(fallback).findServiceDefinitions(Engine.class);
        }

        @Test
        void testFindServiceDefinitionsWhenServiceDefinitionRegistryAndFallbackDoesNotHaveService() {

            when(serviceDefinitionRegistry.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of());
            when(fallback.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of());

            final List<ServiceDefinition<Engine>> serviceDefinitions =
                    fallbackServiceDefinitionRegistry.findServiceDefinitions(Engine.class);

            assertEquals(List.of(), serviceDefinitions);
            verify(serviceDefinitionRegistry).findServiceDefinitions(Engine.class);
            verify(fallback).findServiceDefinitions(Engine.class);
        }
    }
}
