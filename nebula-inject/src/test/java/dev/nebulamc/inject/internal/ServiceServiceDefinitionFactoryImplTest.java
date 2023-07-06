package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Service;
import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceException;
import dev.nebulamc.inject.ServiceFinder;
import dev.nebulamc.inject.car.Car;
import dev.nebulamc.inject.car.CarFactory;
import dev.nebulamc.inject.car.Engine;
import dev.nebulamc.inject.car.Suv;
import dev.nebulamc.inject.car.V8Engine;
import dev.nebulamc.inject.car.Wheels;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ServiceServiceDefinitionFactoryImplTest {

    ServiceServiceDefinitionFactory serviceServiceDefinitionFactory;

    @BeforeEach
    void setUp() {

        serviceServiceDefinitionFactory = new ServiceServiceDefinitionFactoryImpl();
    }

    @Nested
    @DisplayName("createServiceDefinition()")
    class CreateServiceDefinition {

        Object factory;
        Method method;

        @BeforeEach
        void setUp() throws NoSuchMethodException {

            factory = new CarFactory();
            method = CarFactory.class.getDeclaredMethod("createCar", Engine.class, Wheels.class);
        }

        @SuppressWarnings("ConstantConditions")
        @Test
        void testCreateServiceDefinitionWhenFactoryIsNull() {

            assertThrows(NullPointerException.class, () -> serviceServiceDefinitionFactory
                    .createServiceDefinition(null, method));
        }

        @SuppressWarnings("ConstantConditions")
        @Test
        void testCreateServiceDefinitionWhenServiceMethodIsNull() {

            assertThrows(NullPointerException.class, () -> serviceServiceDefinitionFactory
                    .createServiceDefinition(factory, null));
        }

        @Test
        void testCreateServiceWhenServiceMethodIsNotMemberOfFactory() {

            assertThrows(ClassCastException.class, () -> serviceServiceDefinitionFactory
                    .createServiceDefinition(new Object(), method));
        }

        @Nested
        @DisplayName("getServiceType()")
        class GetServiceType {

            @Test
            void testGetServiceType() {

                final ServiceDefinition<?> serviceDefinition =
                        serviceServiceDefinitionFactory.createServiceDefinition(factory, method);

                assertEquals(Car.class, serviceDefinition.getServiceType());
            }
        }

        @Nested
        @DisplayName("createService()")
        class CreateService {

            ServiceFinder serviceFinder;

            @BeforeEach
            void setUp() {

                serviceFinder = mock();
            }

            @AfterEach
            void tearDown() {

                verifyNoMoreInteractions(serviceFinder);
            }

            @SuppressWarnings("ConstantConditions")
            @Test
            void testCreateServiceWhenServiceFinderIsNull() {

                final ServiceDefinition<?> serviceDefinition =
                        serviceServiceDefinitionFactory.createServiceDefinition(factory, method);

                assertThrows(NullPointerException.class,
                        () -> serviceDefinition.createService(null));
            }

            @Test
            void testCreateServiceWhenServiceMethodThrowsException()
                    throws NoSuchMethodException {

                @Factory
                class ThrowingFactory {

                    @Service
                    Wheels createWheels() {

                        throw new RuntimeException();
                    }
                }

                final Method throwingMethod = ThrowingFactory.class
                        .getDeclaredMethod("createWheels");

                final ServiceDefinition<?> serviceDefinition = serviceServiceDefinitionFactory
                        .createServiceDefinition(new ThrowingFactory(), throwingMethod);

                assertThrows(ServiceException.class,
                        () -> serviceDefinition.createService(serviceFinder));
            }

            @Test
            void testCreateServiceWhenMethodReturnsNull() throws NoSuchMethodException {

                @Factory
                class NullFactory {

                    @Service
                    Wheels createWheels() {

                        return null;
                    }
                }

                final Method nullMethod = NullFactory.class
                        .getDeclaredMethod("createWheels");

                final ServiceDefinition<?> serviceDefinition = serviceServiceDefinitionFactory
                        .createServiceDefinition(new NullFactory(), nullMethod);

                assertThrows(ServiceException.class,
                        () -> serviceDefinition.createService(serviceFinder));
            }

            @SuppressWarnings("unchecked")
            @Test
            void testCreateService() {

                final Engine engine = new V8Engine();
                final Wheels wheels = new Wheels();
                when(serviceFinder.findService(Engine.class)).thenReturn(engine);
                when(serviceFinder.findService(Wheels.class)).thenReturn(wheels);

                final ServiceDefinition<Car> serviceDefinition = (ServiceDefinition<Car>)
                        serviceServiceDefinitionFactory.createServiceDefinition(factory, method);

                final Car car = serviceDefinition.createService(serviceFinder);

                assertInstanceOf(Suv.class, car);
                assertEquals(engine, car.getEngine());
                assertEquals(wheels, car.getWheels());
                assertNotNull(car.getWheels());
                verify(serviceFinder).findService(Engine.class);
                verify(serviceFinder).findService(Wheels.class);
            }
        }
    }
}
