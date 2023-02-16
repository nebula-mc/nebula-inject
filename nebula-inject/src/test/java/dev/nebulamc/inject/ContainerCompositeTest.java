package dev.nebulamc.inject;

import dev.nebulamc.inject.car.Engine;
import dev.nebulamc.inject.car.V8Engine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ContainerCompositeTest {

    Container container1;
    Container container2;
    Container composite;

    @BeforeEach
    void setUp() {

        container1 = mock();
        container2 = mock();
        composite = new ContainerComposite(List.of(container1, container2));
    }

    @AfterEach
    void tearDown() {

        verifyNoMoreInteractions(container1, container2);
    }

    @DisplayName("<init>(List<Container>)")
    @Nested
    class Init {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testInitWhenContainersIsNull() {

            assertThrows(NullPointerException.class, () -> new ContainerComposite(null));
        }

        @Test
        void testInitWhenContainersContainsNull() {

            final List<Container> containers = Collections.singletonList(null);

            assertThrows(NullPointerException.class, () -> new ContainerComposite(containers));
        }
    }

    @DisplayName("findServiceDefinition(Class<T>)")
    @Nested
    class FindServiceDefinition {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceDefinitionsWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () -> composite.findServiceDefinition(null));
        }

        @Test
        void testFindServiceDefinitionsWhenCompositesHaveNoServices() {

            when(container1.findServiceDefinitions(Engine.class)).thenReturn(List.of());
            when(container2.findServiceDefinitions(Engine.class)).thenReturn(List.of());

            assertThrows(NoUniqueServiceException.class,
                    () -> composite.findServiceDefinition(Engine.class));
            verify(container1).findServiceDefinitions(Engine.class);
            verify(container2).findServiceDefinitions(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceDefinitionsWhenCompositesHaveMultipleServices() {

            final ServiceDefinition<Engine> serviceDefinition = mock();

            when(container1.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));
            when(container2.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));

            assertThrows(NoUniqueServiceException.class,
                    () -> composite.findServiceDefinition(Engine.class));
            verify(container1).findServiceDefinitions(Engine.class);
            verify(container2).findServiceDefinitions(Engine.class);
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindService() {

            final ServiceDefinition<Engine> serviceDefinition = mock();

            when(container1.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition));
            when(container2.findServiceDefinitions(Engine.class)).thenReturn(List.of());

            assertEquals(serviceDefinition, composite.findServiceDefinition(Engine.class));
            verify(container1).findServiceDefinitions(Engine.class);
            verify(container2).findServiceDefinitions(Engine.class);
        }
    }

    @DisplayName("findServiceDefinitions(Class<T>)")
    @Nested
    class FindServiceDefinitions {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceDefinitionsWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () -> composite.findServiceDefinitions(null));
        }

        @SuppressWarnings("unchecked")
        @Test
        void testFindServiceDefinitions() {

            final ServiceDefinition<Engine> serviceDefinition1 = mock();
            final ServiceDefinition<Engine> serviceDefinition2 = mock();

            when(container1.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition1));
            when(container2.findServiceDefinitions(Engine.class))
                    .thenReturn(List.of(serviceDefinition2));

            assertEquals(List.of(serviceDefinition1, serviceDefinition2),
                    composite.findServiceDefinitions(Engine.class));
            verify(container1).findServiceDefinitions(Engine.class);
            verify(container2).findServiceDefinitions(Engine.class);
        }
    }

    @DisplayName("findService(Class<T>)")
    @Nested
    class FindService {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServiceWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () -> composite.findService(null));
        }

        @Test
        void testFindServiceWhenCompositesHaveNoServices() {

            when(container1.findServices(Engine.class)).thenReturn(List.of());
            when(container2.findServices(Engine.class)).thenReturn(List.of());

            assertThrows(NoUniqueServiceException.class,
                    () -> composite.findService(Engine.class));
            verify(container1).findServices(Engine.class);
            verify(container2).findServices(Engine.class);
        }

        @Test
        void testFindServiceWhenCompositesHaveMultipleServices() {

            final Engine engine = new V8Engine();

            when(container1.findServices(Engine.class))
                    .thenReturn(List.of(engine));
            when(container2.findServices(Engine.class))
                    .thenReturn(List.of(engine));

            assertThrows(NoUniqueServiceException.class,
                    () -> composite.findService(Engine.class));
            verify(container1).findServices(Engine.class);
            verify(container2).findServices(Engine.class);
        }

        @Test
        void testFindService() {

            final Engine engine = new V8Engine();

            when(container1.findServices(Engine.class))
                    .thenReturn(List.of(engine));
            when(container2.findServices(Engine.class)).thenReturn(List.of());

            assertEquals(engine, composite.findService(Engine.class));
            verify(container1).findServices(Engine.class);
            verify(container2).findServices(Engine.class);
        }
    }

    @DisplayName("findOptionalService(Class<T>)")
    @Nested
    class FindOptionalService {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindOptionalServiceWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () -> composite.findOptionalService(null));
        }

        @Test
        void testFindOptionalServiceWhenCompositesHaveNoServices() {

            when(container1.findServices(Engine.class)).thenReturn(List.of());
            when(container2.findServices(Engine.class)).thenReturn(List.of());

            assertEquals(Optional.empty(), composite.findOptionalService(Engine.class));
            verify(container1).findServices(Engine.class);
            verify(container2).findServices(Engine.class);
        }

        @Test
        void testFindOptionalServiceWhenCompositesHaveMultipleServices() {

            final Engine engine = new V8Engine();

            when(container1.findServices(Engine.class))
                    .thenReturn(List.of(engine));
            when(container2.findServices(Engine.class))
                    .thenReturn(List.of(engine));

            assertEquals(Optional.empty(), composite.findOptionalService(Engine.class));
            verify(container1).findServices(Engine.class);
            verify(container2).findServices(Engine.class);
        }

        @Test
        void testFindOptionalService() {

            final Engine engine = new V8Engine();

            when(container1.findServices(Engine.class))
                    .thenReturn(List.of(engine));
            when(container2.findServices(Engine.class)).thenReturn(List.of());

            assertEquals(Optional.of(engine), composite.findOptionalService(Engine.class));
            verify(container1).findServices(Engine.class);
            verify(container2).findServices(Engine.class);
        }
    }

    @DisplayName("findServices(Class<T>)")
    @Nested
    class FindServices {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testFindServicesWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () -> composite.findServices(null));
        }

        @Test
        void testFindServices() {

            final Engine engine = new V8Engine();

            when(container1.findServices(Engine.class))
                    .thenReturn(List.of(engine));
            when(container2.findServices(Engine.class))
                    .thenReturn(List.of(engine, engine));

            assertEquals(List.of(engine, engine, engine), composite.findServices(Engine.class));
            verify(container1).findServices(Engine.class);
            verify(container2).findServices(Engine.class);
        }
    }
}
