package dev.nebulamc.inject.internal;

import org.jspecify.nullness.NullMarked;

import java.lang.reflect.Method;

import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Service;
import dev.nebulamc.inject.ServiceDefinition;

/**
 * A factory for creating {@link ServiceDefinition ServiceDefinitions} for {@link Service} methods.
 *
 * @author Sparky983
 * @see Service
 * @see Factory
 */
@NullMarked
public interface ServiceServiceDefinitionFactory {

    /**
     * Creates a new {@link ServiceDefinition} for the specified {@link Service} method.
     *
     * @param factory the factory
     * @param serviceMethod the service method
     * @return the service definition
     * @throws ClassCastException if the service method is not a member of the factory's class.
     * @throws NullPointerException if the service method or factory are {@code null}.
     */
    ServiceDefinition<?> createServiceDefinition(Object factory, Method serviceMethod);
}
