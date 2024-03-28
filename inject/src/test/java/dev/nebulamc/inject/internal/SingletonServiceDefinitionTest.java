package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceFinder;
import dev.nebulamc.inject.car.Engine;
import dev.nebulamc.inject.car.V8Engine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class SingletonServiceDefinitionTest {

    Engine engine;
    ServiceDefinition<Engine> serviceDefinition;

    @BeforeEach
    void setUp() {

        engine = new V8Engine();
        serviceDefinition = new SingletonServiceDefinition<>(Engine.class, engine);
    }

    @DisplayName("<init>(Class<T>, T)")
    @Nested
    class Init {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testInitWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () ->
                    new SingletonServiceDefinition<>(null, engine));
        }

        @SuppressWarnings("ConstantConditions")
        @Test
        void testInitWhenSingletonIsNull() {

            assertThrows(NullPointerException.class, () ->
                    new SingletonServiceDefinition<>(Engine.class, null));
        }
    }

    @DisplayName("getServiceType()")
    @Nested
    class GetServiceType {

        @Test
        void testGetServiceType() {

            assertEquals(Engine.class, serviceDefinition.getServiceType());
        }
    }

    @DisplayName("createService(ServiceFinder)")
    @Nested
    class CreateService {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testCreateServiceWhenServiceFinderIsNull() {

            assertThrows(NullPointerException.class, () ->
                    serviceDefinition.createService(null));
        }

        @Test
        void testCreateService() {

            final ServiceFinder serviceFinder = mock();

            assertEquals(engine, serviceDefinition.createService(serviceFinder));
            verifyNoMoreInteractions(serviceFinder);
        }
    }
}
