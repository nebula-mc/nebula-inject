package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.NoUniqueServiceException;
import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceDefinitionRegistry;
import dev.nebulamc.inject.car.Engine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ServiceDefinitionRegistryCompositeTest {

    ServiceDefinitionRegistry serviceDefinitionRegistry1;
    ServiceDefinitionRegistry serviceDefinitionRegistry2;
    ServiceDefinitionRegistry composite;

    @BeforeEach
    void setUp() {

        serviceDefinitionRegistry1 = mock();
        serviceDefinitionRegistry2 = mock();
        composite = new ServiceDefinitionRegistryComposite(
                List.of(serviceDefinitionRegistry1, serviceDefinitionRegistry2));
    }

    @AfterEach
    void tearDown() {

        verifyNoMoreInteractions(serviceDefinitionRegistry1, serviceDefinitionRegistry2);
    }

    @DisplayName("<init>(List<ServiceDefinitionRegistry>)")
    @Nested
    class Init {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testInitWhenRegistriesIsNull() {

            assertThrows(NullPointerException.class,
                    () -> new ServiceDefinitionRegistryComposite(null));
        }

        @Test
        void testInitWhenRegistriesContainsNull() {

            final List<ServiceDefinitionRegistry> serviceDefinitionRegistries =
                    Collections.singletonList(null);

            assertThrows(NullPointerException.class,
                    () -> new ServiceDefinitionRegistryComposite(serviceDefinitionRegistries));
        }
    }

    @DisplayName("findServiceDefinition()")
    @Nested
    class FindServiceDefinition {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceDefinitionsWhenTypeIsNull() {

            assertThrows(NullPointerException.class,
                    () -> composite.findServiceDefinitions(null));
        }

        @Test
        void testFindServiceDefinitionsWhenCompositesHaveNoServices() {

            when(serviceDefinitionRegistry1.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of());
            when(serviceDefinitionRegistry2.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of());

            assertThrows(NoUniqueServiceException.class,
                    () -> composite.findServiceDefinition(Engine.class));
            verify(serviceDefinitionRegistry1).findServiceDefinitions(Engine.class);
            verify(serviceDefinitionRegistry2).findServiceDefinitions(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceDefinitionsWhenCompositesHaveMultipleServices() {

            final ServiceDefinition<Engine> serviceDefinition = mock();

            when(serviceDefinitionRegistry1.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));
            when(serviceDefinitionRegistry2.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));

            assertThrows(NoUniqueServiceException.class,
                    () -> composite.findServiceDefinition(Engine.class));
            verify(serviceDefinitionRegistry1).findServiceDefinitions(Engine.class);
            verify(serviceDefinitionRegistry2).findServiceDefinitions(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindService() {

            final ServiceDefinition<Engine> serviceDefinition = mock();

            when(serviceDefinitionRegistry1.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));
            when(serviceDefinitionRegistry2.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of());

            assertEquals(serviceDefinition, composite.findServiceDefinition(Engine.class));
            verify(serviceDefinitionRegistry1).findServiceDefinitions(Engine.class);
            verify(serviceDefinitionRegistry2).findServiceDefinitions(Engine.class);
        }
    }

    @DisplayName("findServiceDefinitions()")
    @Nested
    class FindServiceDefinitions {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceDefinitionsWhenTypeIsNull() {

            assertThrows(NullPointerException.class,
                    () -> composite.findServiceDefinitions(null));
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceDefinitions() {

            final ServiceDefinition<Engine> serviceDefinition1 = mock();
            final ServiceDefinition<Engine> serviceDefinition2 = mock();

            when(serviceDefinitionRegistry1.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition1));
            when(serviceDefinitionRegistry2.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition2));

            assertEquals(List.of(serviceDefinition1, serviceDefinition2),
                    composite.findServiceDefinitions(Engine.class));
            verify(serviceDefinitionRegistry1).findServiceDefinitions(Engine.class);
            verify(serviceDefinitionRegistry2).findServiceDefinitions(Engine.class);
        }
    }
}
