package dev.nebulamc.inject;

import dev.nebulamc.inject.car.Car;
import dev.nebulamc.inject.car.Engine;
import dev.nebulamc.inject.car.V12Engine;
import dev.nebulamc.inject.car.V8Engine;
import dev.nebulamc.inject.car.Wheels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FactoryServiceDefinitionRegistryServiceImplTest {

    ServiceServiceDefinitionFactory serviceServiceDefinitionFactory;
    FactoryServiceDefinitionRegistryFactory serviceDefinitionRegistryFactory;

    @BeforeEach
    void setUp() {

        serviceServiceDefinitionFactory = mock();
        serviceDefinitionRegistryFactory = new FactoryServiceDefinitionRegistryFactoryImpl(serviceServiceDefinitionFactory);
    }

    @DisplayName("<init>(ServiceServiceDefinitionFactory)")
    @Nested
    class Init {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testInitWhenFactoryIsNull() {

            assertThrows(NullPointerException.class,
                    () -> new FactoryServiceDefinitionRegistryFactoryImpl(null));
        }
    }

    @DisplayName("createServiceDefinitionRegistry(Object)")
    @Nested
    class CreateServiceDefinitionRegistry {

        @Test
        void testCreateServiceDefinitionRegistryWhenFactoryIsNotAnnotated() {

            class UnannotatedFactory {

            }

            final UnannotatedFactory factory = new UnannotatedFactory();

            assertThrows(IllegalArgumentException.class, () -> serviceDefinitionRegistryFactory
                    .createServiceDefinitionRegistry(factory));
        }

        @SuppressWarnings("ConstantConditions")
        @Test
        void testCreateServiceDefinitionRegistryWhenFactoryIsNull() {

            assertThrows(NullPointerException.class, () -> serviceDefinitionRegistryFactory
                    .createServiceDefinitionRegistry(null));
        }

        @DisplayName("findServiceDefinition(Class<T>)")
        @Nested
        class FindServiceDefinition {

            @SuppressWarnings("unchecked")
            @Test
            void testFindServiceDefinitionWhenMultipleServices() throws NoSuchMethodException {

                @Factory
                class MultipleEngineFactory {

                    @Service
                    public Engine createV8Engine() {

                        return new V8Engine();
                    }

                    @Service
                    public Engine createV12Engine() {

                        return new V12Engine();
                    }
                }

                final MultipleEngineFactory factory = new MultipleEngineFactory();

                final Method createV8EngineMethod = MultipleEngineFactory.class
                        .getDeclaredMethod("createV8Engine");
                final Method createV12EngineMethod = MultipleEngineFactory.class
                        .getDeclaredMethod("createV12Engine");
                final ServiceDefinition<Engine> engineServiceDefinition = mock();
                when(engineServiceDefinition.getServiceType()).thenReturn(Engine.class);

                when(serviceServiceDefinitionFactory
                        .createServiceDefinition(factory, createV8EngineMethod))
                        .thenAnswer((invocation) -> engineServiceDefinition);
                when(serviceServiceDefinitionFactory
                        .createServiceDefinition(factory, createV12EngineMethod))
                        .thenAnswer((invocation) -> engineServiceDefinition);

                final ServiceDefinitionRegistry serviceDefinitionRegistry =
                        serviceDefinitionRegistryFactory.createServiceDefinitionRegistry(factory);

                assertThrows(NoUniqueServiceException.class, () -> serviceDefinitionRegistry
                        .findServiceDefinition(Engine.class));
                verify(serviceServiceDefinitionFactory)
                        .createServiceDefinition(factory, createV8EngineMethod);
                verify(serviceServiceDefinitionFactory)
                        .createServiceDefinition(factory, createV12EngineMethod);
            }

            @Test
            void testFindServiceDefinitionWhenNoServices() {

                @Factory
                class NoServicesFactory {

                }

                final ServiceDefinitionRegistry serviceDefinitionRegistry =
                        serviceDefinitionRegistryFactory.createServiceDefinitionRegistry(
                                new NoServicesFactory());

                assertNotNull(serviceDefinitionRegistry);
                assertThrows(NoUniqueServiceException.class, () -> serviceDefinitionRegistry
                        .findServiceDefinition(Car.class));
            }

            @SuppressWarnings("ConstantConditions")
            @Test
            void testFindServiceDefinitionWhenTypeIsNull() {

                @Factory
                class TestFactory {

                }

                final ServiceDefinitionRegistry serviceDefinitionRegistry =
                        serviceDefinitionRegistryFactory.createServiceDefinitionRegistry(
                                new TestFactory());

                assertNotNull(serviceDefinitionRegistry);
                assertThrows(NullPointerException.class, () -> serviceDefinitionRegistry
                        .findServiceDefinition(null));
            }

            @SuppressWarnings("unchecked")
            @Test
            void testFindServiceDefinition() throws NoSuchMethodException {

                @Factory
                class WheelsFactory {

                    @Service
                    public Wheels createWheels() {

                        return new Wheels();
                    }
                }

                final WheelsFactory factory = new WheelsFactory();

                final Method createWheelsMethod = WheelsFactory.class
                        .getDeclaredMethod("createWheels");
                final ServiceDefinition<Wheels> wheelsServiceDefinition = mock();
                when(wheelsServiceDefinition.getServiceType()).thenReturn(Wheels.class);

                when(serviceServiceDefinitionFactory
                        .createServiceDefinition(factory, createWheelsMethod))
                        .thenAnswer((invocation) -> wheelsServiceDefinition);

                final ServiceDefinitionRegistry serviceDefinitionRegistry =
                        serviceDefinitionRegistryFactory.createServiceDefinitionRegistry(factory);

                assertEquals(wheelsServiceDefinition,
                        serviceDefinitionRegistry.findServiceDefinition(Wheels.class));
                verify(serviceServiceDefinitionFactory)
                        .createServiceDefinition(factory, createWheelsMethod);
            }
        }

        @DisplayName("findServiceDefinitions(Class<T>)")
        @Nested
        class FindServiceDefinitions {

            @Test
            void testFindServiceDefinitionsWhenNoServices() {

                @Factory
                class NoServicesFactory {

                }

                final ServiceDefinitionRegistry serviceDefinitionRegistry =
                        serviceDefinitionRegistryFactory.createServiceDefinitionRegistry(
                                new NoServicesFactory());

                final List<ServiceDefinition<Car>> serviceDefinitions =
                        serviceDefinitionRegistry.findServiceDefinitions(Car.class);

                assertEquals(List.of(), serviceDefinitions);
            }

            @SuppressWarnings("ConstantConditions")
            @Test
            void testFindServiceDefinitionsWhenTypeIsNull() {

                @Factory
                class TestFactory {

                }

                final ServiceDefinitionRegistry serviceDefinitionRegistry =
                        serviceDefinitionRegistryFactory.createServiceDefinitionRegistry(
                                new TestFactory());

                assertThrows(NullPointerException.class, () -> serviceDefinitionRegistry
                        .findServiceDefinitions(null));
            }

            @SuppressWarnings("unchecked")
            @Test
            void testFindServiceDefinitions() throws NoSuchMethodException {

                @Factory
                class EngineFactory {

                    @Service
                    public Engine createV8Engine() {

                        return new V8Engine();
                    }

                    @Service
                    public Engine createV12Engine() {

                        return new V12Engine();
                    }
                }

                final EngineFactory factory = new EngineFactory();

                final Method createV8EngineMethod = EngineFactory.class
                        .getDeclaredMethod("createV8Engine");
                final Method createV12EngineMethod = EngineFactory.class
                        .getDeclaredMethod("createV12Engine");

                final ServiceDefinition<Engine> engineServiceDefinition = mock();
                when(engineServiceDefinition.getServiceType()).thenReturn(Engine.class);
                when(serviceServiceDefinitionFactory
                        .createServiceDefinition(factory, createV8EngineMethod))
                        .thenAnswer((invocation) -> engineServiceDefinition);
                when(serviceServiceDefinitionFactory
                        .createServiceDefinition(factory, createV12EngineMethod))
                        .thenAnswer((invocation) -> engineServiceDefinition);

                final ServiceDefinitionRegistry serviceDefinitionRegistry =
                        serviceDefinitionRegistryFactory.createServiceDefinitionRegistry(factory);

                final List<ServiceDefinition<Engine>> serviceDefinitions =
                        serviceDefinitionRegistry.findServiceDefinitions(Engine.class);

                assertEquals(List.of(engineServiceDefinition, engineServiceDefinition), serviceDefinitions);

                // Verify that the list is unmodifiable
                assertThrows(UnsupportedOperationException.class, () -> serviceDefinitions
                        .add(engineServiceDefinition));
            }
        }
    }
}
