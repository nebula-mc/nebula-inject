package dev.nebulamc.inject;

import dev.nebulamc.inject.car.Car;
import dev.nebulamc.inject.car.CarFactory;
import dev.nebulamc.inject.car.Engine;
import dev.nebulamc.inject.car.Sedan;
import dev.nebulamc.inject.car.V8Engine;
import dev.nebulamc.inject.car.Wheels;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ContainerTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    void testParentWhenParentIsNull() {

        final Container.Builder builder = Container.builder();

        assertThrows(NullPointerException.class, () -> builder.parent(null));
    }

    @Test
    void testParent() {

        final Engine engine = new V8Engine();
        final Wheels wheels = new Wheels();
        final Container parent = mock();
        when(parent.findServices(Engine.class)).thenReturn(List.of(engine));
        when(parent.findServices(Wheels.class)).thenReturn(List.of(wheels, wheels));

        final Container child = Container.builder()
                .parent(parent)
                .singleton(Wheels.class, wheels)
                .build();

        assertEquals(engine, child.findService(Engine.class));
        assertEquals(List.of(wheels, wheels, wheels), child.findServices(Wheels.class));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testSingletonWhenSingletonIsNull() {

        final Container.Builder builder = Container.builder();

        assertThrows(NullPointerException.class, () -> builder
                .singleton((Class<? super Object>) null, null));
    }

    @Test
    void testSingleton() {

        final V8Engine engine = new V8Engine();
        final Wheels wheels = new Wheels();

        final Container container = Container.builder()
                .singleton(Engine.class, engine)
                .singleton(Wheels.class, wheels)
                .build();

        assertEquals(engine, container.findService(Engine.class));
        assertEquals(wheels, container.findService(Wheels.class));
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
                .singleton(Engine.class, engine)
                .singleton(Wheels.class, wheels)
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

        when(serviceDefinition.createService(any())).thenReturn(engine);

        assertEquals(serviceDefinition, container.findServiceDefinition(Engine.class));
        assertEquals(engine, container.findService(Engine.class));

        verify(serviceDefinition).getServiceType();
        verify(serviceDefinition).createService(any());
        verifyNoMoreInteractions(serviceDefinition);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testServiceDefinitionRegistryWhenServiceDefinitionRegistryIsNull() {

        final Container.Builder builder = Container.builder();

        assertThrows(NullPointerException.class, () -> builder.serviceDefinitionRegistry(null));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testServiceDefinitionRegistry() {

        final Engine engine = new V8Engine();
        final ServiceDefinition<Engine> serviceDefinition = mock();
        when(serviceDefinition.getServiceType()).thenReturn(Engine.class);

        final ServiceDefinitionRegistry serviceDefinitionRegistry = ServiceDefinitionRegistry.builder()
                .serviceDefinition(serviceDefinition)
                .build();

        final Container container = Container.builder()
                .serviceDefinitionRegistry(serviceDefinitionRegistry)
                .build();

        when(serviceDefinition.createService(any())).thenReturn(engine);

        assertEquals(serviceDefinition, container.findServiceDefinition(Engine.class));
        assertEquals(engine, container.findService(Engine.class));
    }

    @Test
    void testFindConcreteService() {

        final Engine engine = new V8Engine();
        final Wheels wheels = new Wheels();

        final Container container = Container.builder()
                .singleton(Engine.class, engine)
                .singleton(Wheels.class, wheels)
                .build();

        final Car car = container.findService(Sedan.class);

        assertEquals(engine, car.getEngine());
        assertEquals(wheels, car.getWheels());
    }

    @Test
    void testFindConcreteServiceWithParent() {

        final Wheels wheels = new Wheels();
        final Container parent = Container.builder()
                .singleton(Wheels.class, wheels)
                .build();
        final Container child = Container.builder()
                .parent(parent)
                .build();

        assertEquals(wheels, child.findService(Wheels.class));
    }

    @Test
    void testFindServiceWhenServiceIsNotRegistered() {

        final Container container = Container.create();

        assertEquals(Optional.empty(), container.findOptionalService(Car.class));
    }

    @Test
    void testFindServiceWhenContainer() {

        final Container container = Container.create();

        assertEquals(container, container.findService(Container.class));
    }
}
