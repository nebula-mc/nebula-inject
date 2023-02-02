package dev.nebulamc.inject;

import dev.nebulamc.inject.car.Engine;
import dev.nebulamc.inject.car.V8Engine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class SingletonServiceDefinitionTest {

    Engine engine;
    ServiceDefinition<Engine> serviceDefinition;

    @BeforeEach
    void setUp() {

        engine = new V8Engine();
        serviceDefinition = new SingletonServiceDefinition<>(engine, Engine.class);
    }

    @DisplayName("<init>(T, Class<? super T>)")
    @Nested
    class Init {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testInitWhenSingletonIsNull() {

            assertThrows(NullPointerException.class, () ->
                    new SingletonServiceDefinition<>(null, Engine.class));
        }

        @SuppressWarnings("ConstantConditions")
        @Test
        void testInitWhenTypeIsNull() {

            assertThrows(NullPointerException.class, () ->
                    new SingletonServiceDefinition<>(engine, null));
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

    @DisplayName("createObject(ServiceFinder)")
    @Nested
    class CreateObject {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testCreateObjectWhenServiceFinderIsNull() {

            assertThrows(NullPointerException.class, () ->
                    serviceDefinition.createObject(null));
        }

        @Test
        void testCreateObject() {

            final ServiceFinder serviceFinder = mock();

            assertEquals(engine, serviceDefinition.createObject(serviceFinder));
            verifyNoMoreInteractions(serviceFinder);
        }
    }
}