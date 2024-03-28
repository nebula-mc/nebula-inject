package dev.nebulamc.inject;

import dev.nebulamc.inject.car.Engine;
import dev.nebulamc.inject.car.V8Engine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceDefinitionRegistryTest {

    @Test
    void testEmpty() {

        final ServiceDefinitionRegistry registry = ServiceDefinitionRegistry.builder()
                .build();

        assertEquals(List.of(), registry.findServiceDefinitions(Engine.class));
        assertThrows(NoUniqueServiceException.class, () ->
                registry.findServiceDefinition(Engine.class));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testServiceDefinitionWhenServiceDefinitionIsNull() {

        assertThrows(NullPointerException.class, () ->
                ServiceDefinitionRegistry.builder().serviceDefinition(null));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testFindServiceDefinitionWhenTypeIsNull() {

        final ServiceDefinitionRegistry registry = ServiceDefinitionRegistry.builder()
                .build();

        assertThrows(NullPointerException.class, () -> registry.findServiceDefinition(null));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testServiceDefinition() {

        final ServiceDefinition<Engine> serviceDefinition = mock();
        when(serviceDefinition.getServiceType()).thenReturn(Engine.class);

        final ServiceDefinitionRegistry registry = ServiceDefinitionRegistry.builder()
                .serviceDefinition(serviceDefinition)
                .build();

        assertEquals(serviceDefinition, registry.findServiceDefinition(Engine.class));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testServiceDefinitionRegistryWhenServiceDefinitionRegistryIsNull() {

        final ServiceDefinitionRegistry.Builder builder = ServiceDefinitionRegistry.builder();

        assertThrows(NullPointerException.class, () -> builder.serviceDefinitionRegistry(null));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testServiceDefinitionRegistry() {

        final ServiceDefinition<Engine> serviceDefinition = mock();
        when(serviceDefinition.getServiceType()).thenReturn(Engine.class);

        final ServiceDefinitionRegistry child = ServiceDefinitionRegistry.builder()
                .serviceDefinition(serviceDefinition)
                .build();

        final ServiceDefinitionRegistry parent = ServiceDefinitionRegistry.builder()
                .serviceDefinitionRegistry(child)
                .build();

        assertEquals(parent.findServiceDefinition(Engine.class), serviceDefinition);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testFindServicesDefinitionsWhenTypeIsNull() {

        final ServiceDefinitionRegistry registry = ServiceDefinitionRegistry.builder()
                .build();

        assertThrows(NullPointerException.class, () -> registry.findServiceDefinitions(null));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testMultipleServiceDefinitions() {

        final ServiceDefinition<Engine> serviceDefinition = mock();
        when(serviceDefinition.getServiceType()).thenReturn(Engine.class);

        final ServiceDefinitionRegistry registry = ServiceDefinitionRegistry.builder()
                .serviceDefinition(serviceDefinition)
                .serviceDefinition(serviceDefinition)
                .build();

        assertEquals(List.of(serviceDefinition, serviceDefinition),
                registry.findServiceDefinitions(Engine.class));
        assertThrows(NoUniqueServiceException.class, () ->
                registry.findServiceDefinition(Engine.class));
    }
}
