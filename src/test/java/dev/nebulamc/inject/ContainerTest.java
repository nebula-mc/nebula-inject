package dev.nebulamc.inject;

import dev.nebulamc.inject.car.Car;
import dev.nebulamc.inject.car.CarFactory;
import dev.nebulamc.inject.car.Engine;
import dev.nebulamc.inject.car.Suv;
import dev.nebulamc.inject.car.V8Engine;
import dev.nebulamc.inject.car.Wheels;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ContainerTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    void testSingletonWhenSingletonIsNull() {

        final Container.Builder builder = Container.builder();

        assertThrows(NullPointerException.class, () -> builder.singleton(null));
        assertThrows(NullPointerException.class, () -> builder
                .singleton(null, (Iterable<Class<? super Object>>) null));
        assertThrows(NullPointerException.class, () -> builder
                .singleton(null, (Class<? super Object>) null));
    }

    @Test
    void testSingleton() {

        final V8Engine engine = new V8Engine();
        final Wheels wheels = new Wheels();
        final Suv suv = new Suv(engine, wheels);

        final Container container = Container.builder()
                .singleton(engine)
                .singleton(wheels, Wheels.class)
                .singleton(suv, List.of(Suv.class, Car.class))
                .build();

        assertEquals(engine, container.findService(V8Engine.class));
        assertEquals(engine, container.findService(Object.class));
        assertEquals(engine, container.findService(Engine.class));
        assertEquals(wheels, container.findService(Wheels.class));
        assertEquals(suv, container.findService(Suv.class));
        assertEquals(suv, container.findService(Car.class));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testFactoryWhenFactoryIsNull() {

        final Container.Builder builder = Container.builder();

        assertThrows(NullPointerException.class, () -> builder.factory(null));
    }

    @Test
    void testFactory() {

        final Engine engine = new V8Engine();
        final Wheels wheels = new Wheels();

        final Container container = Container.builder()
                .singleton(engine)
                .singleton(wheels)
                .factory(new CarFactory())
                .build();

        final Car car = container.findService(Car.class);

        assertEquals(engine, car.getEngine());
        assertEquals(wheels, car.getWheels());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testServiceDefinitionWhenServiceDefinitionIsNull() {

        final Container.Builder builder = Container.builder();

        assertThrows(NullPointerException.class, () -> builder.serviceDefinition(null));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testServiceDefinition() {

        final Engine engine = new V8Engine();
        final ServiceDefinition<Engine> serviceDefinition = mock();
        when(serviceDefinition.getServiceType()).thenReturn(Engine.class);

        final Container container = Container.builder()
                .serviceDefinition(serviceDefinition)
                .build();

        when(serviceDefinition.createObject(container)).thenReturn(engine);

        assertEquals(serviceDefinition, container.findServiceDefinition(Engine.class));
        assertEquals(engine, container.findService(Engine.class));

        verify(serviceDefinition).getServiceType();
        verify(serviceDefinition).createObject(container);
        verifyNoMoreInteractions(serviceDefinition);
    }

    @Test
    void testFindConcreteService() {

        final Engine engine = new V8Engine();
        final Wheels wheels = new Wheels();

        final Container container = Container.builder()
                .singleton(engine)
                .singleton(wheels)
                .build();

        final Car car = container.findService(Suv.class);

        assertEquals(engine, car.getEngine());
        assertEquals(wheels, car.getWheels());
    }

    @Test
    void testFindServiceWhenServiceIsNotRegistered() {

        final Container container = Container.create();

        assertEquals(Optional.empty(), container.findOptionalService(Car.class));
    }
}
