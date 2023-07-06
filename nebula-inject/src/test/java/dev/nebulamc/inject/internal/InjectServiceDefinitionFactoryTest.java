package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.NoUniqueServiceException;
import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceException;
import dev.nebulamc.inject.ServiceFinder;
import dev.nebulamc.inject.car.Car;
import dev.nebulamc.inject.car.Engine;
import dev.nebulamc.inject.car.Sedan;
import dev.nebulamc.inject.car.V8Engine;
import dev.nebulamc.inject.car.Wheels;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.annotation.RetentionPolicy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class InjectServiceDefinitionFactoryTest {

    InjectServiceDefinitionFactory serviceDefinitionFactory;
    ServiceDefinition<Car> serviceDefinition;

    @BeforeEach
    void setUp() {

        serviceDefinitionFactory = new InjectServiceDefinitionFactoryImpl();
        serviceDefinition = serviceDefinitionFactory.createServiceDefinition(Car.class, Sedan.class);
    }

    static class NoInjectAnnotation {

        public NoInjectAnnotation(final Car car) {

        }
    }

    static class MultipleInjectableConstructors {

        @Inject
        public MultipleInjectableConstructors(final Car car) {

        }

        @Inject
        public MultipleInjectableConstructors(final Engine engine) {

        }
    }

    static class ThrowingConstructor {

        @Inject
        public ThrowingConstructor() {

            throw new RuntimeException();
        }
    }

    @DisplayName("createServiceDefinition(Class<T>, Class<? extends T>)")
    @Nested
    class CreateServiceDefinition {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testCreateServiceDefinitionFactoryWhenServiceTypeIsNull() {

            assertThrows(NullPointerException.class,
                    () -> serviceDefinitionFactory.createServiceDefinition(null, Object.class));
        }

        @SuppressWarnings("ConstantConditions")
        @Test
        void testCreateServiceDefinitionFactoryWhenImplementationIsNull() {

            assertThrows(NullPointerException.class,
                    () -> serviceDefinitionFactory.createServiceDefinition(Object.class, null));
        }

        @Test
        void testCreateServiceDefinitionFactoryWhenTypeIsAbstract() {

            interface Interface {

            }

            abstract class AbstractClass {

            }

            assertThrows(IllegalArgumentException.class, () -> serviceDefinitionFactory
                    .createServiceDefinition(Object.class, AbstractClass.class));
            assertThrows(IllegalArgumentException.class, () -> serviceDefinitionFactory
                    .createServiceDefinition(Object.class, Interface.class));
        }

        @Test
        void testCreateServiceDefinitionFactoryWhenNoInjectableConstructor() {

            assertThrows(IllegalArgumentException.class, () -> serviceDefinitionFactory
                    .createServiceDefinition(Object.class, NoInjectAnnotation.class));
            assertThrows(IllegalArgumentException.class, () -> serviceDefinitionFactory
                    .createServiceDefinition(Object.class, MultipleInjectableConstructors.class));
            assertThrows(IllegalArgumentException.class, () -> serviceDefinitionFactory
                    .createServiceDefinition(Object.class, int.class));
            assertThrows(IllegalArgumentException.class, () -> serviceDefinitionFactory
                    .createServiceDefinition(Object.class, int[].class));
            assertThrows(IllegalArgumentException.class, () -> serviceDefinitionFactory
                    .createServiceDefinition(Object.class, RetentionPolicy.class));
        }
    }

    @DisplayName("getServiceType()")
    @Nested
    class GetServiceType {

        @Test
        void testGetServiceType() {

            final Class<Car> serviceType = serviceDefinition.getServiceType();

            assertEquals(Car.class, serviceType);
        }
    }

    @DisplayName("createService(ServiceFinder)")
    @Nested
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

            assertThrows(NullPointerException.class,
                    () -> serviceDefinition.createService(null));
        }

        @Test
        void testCreateService() {

            final Engine engine = new V8Engine();
            final Wheels wheels = new Wheels();
            when(serviceFinder.findService(Engine.class)).thenReturn(engine);
            when(serviceFinder.findService(Wheels.class)).thenReturn(wheels);

            final Car car = serviceDefinition.createService(serviceFinder);

            assertEquals(engine, car.getEngine());
            assertEquals(wheels, car.getWheels());
            verify(serviceFinder).findService(Engine.class);
            verify(serviceFinder).findService(Wheels.class);
        }

        @Test
        void testCreateServiceWhenNoArgsConstructor() {

            final ServiceDefinition<Engine> serviceDefinition =
                    serviceDefinitionFactory.createServiceDefinition(Engine.class, V8Engine.class);

            final Engine engine = serviceDefinition.createService(serviceFinder);

            assertInstanceOf(V8Engine.class, engine);
        }

        @Test
        void testCreateServiceWhenThrowingConstructor() {

            final ServiceDefinition<ThrowingConstructor> serviceDefinition =
                    serviceDefinitionFactory.createServiceDefinition(ThrowingConstructor.class, ThrowingConstructor.class);

            assertThrows(ServiceException.class,
                    () -> serviceDefinition.createService(serviceFinder));
        }

        @Test
        void testCreateServiceWhenServiceNotFound() {

            when(serviceFinder.findService(Engine.class))
                    .thenThrow(NoUniqueServiceException.class);

            assertThrows(NoUniqueServiceException.class,
                    () -> serviceDefinition.createService(serviceFinder));

            verify(serviceFinder).findService(Engine.class);
        }
    }
}
