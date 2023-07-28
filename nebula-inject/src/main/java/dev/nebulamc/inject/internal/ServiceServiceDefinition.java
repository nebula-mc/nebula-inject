package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.NoUniqueServiceException;
import dev.nebulamc.inject.Service;
import dev.nebulamc.inject.ServiceDefinition;
import dev.nebulamc.inject.ServiceException;
import dev.nebulamc.inject.ServiceFinder;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.nullness.NullMarked;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * A service definition for a {@link Service} method.
 *
 * @author Sparky983
 * @param <T> the service type
 */
@NullMarked
final class ServiceServiceDefinition<T> implements ServiceDefinition<T> {

    /**
     * Cached method parameters so a new array doesn't need to be allocated by
     * {@link Method#getParameters()} due to defensive copying.
     */
    private final Parameter[] parameters;

    private final Object factory;
    private final Method serviceMethod;
    private final ParameterResolver parameterResolver;

    /**
     * Constructs a new {@link ServiceServiceDefinition} for the specified {@link Service} method.
     *
     * @param factory the factory object
     * @param serviceMethod the service serviceMethod
     * @param parameterResolver the parameter resolver to use
     * @throws ClassCastException if the service method is not a member of the factory's class.
     * @throws NullPointerException if the factory, service method or parameter resolver are
     * {@code null}.
     */
    ServiceServiceDefinition(final Object factory,
                             final Method serviceMethod,
                             final ParameterResolver parameterResolver) {

        Preconditions.requireNonNull(factory, "factory");
        Preconditions.requireNonNull(serviceMethod, "serviceMethod");
        Preconditions.requireNonNull(parameterResolver, "parameterResolver");

        if (!serviceMethod.getDeclaringClass().isInstance(factory)) {
            throw new ClassCastException(
                    "The service method is not a member of the factory's class.");
        }

        this.factory = factory;
        this.serviceMethod = serviceMethod;
        this.parameterResolver = parameterResolver;
        this.parameters = serviceMethod.getParameters();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getServiceType() {

        return (Class<T>) serviceMethod.getReturnType();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T createService(final ServiceFinder serviceFinder) {

        Preconditions.requireNonNull(serviceFinder, "serviceFinder");

        final Object[] arguments = new Object[parameters.length];

        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = parameterResolver.resolveParameter(parameters[i], serviceFinder);
        }

        serviceMethod.setAccessible(true);

        try {
            final T t = (T) serviceMethod.invoke(factory, arguments);
            if (t == null) {
                throw new ServiceException("Service method " + serviceMethod + " returned null");
            }
            return t;
        } catch (final InvocationTargetException e) {
            if (e.getCause() instanceof final NoUniqueServiceException cause) {
                throw cause;
            }
            throw new ServiceException(serviceMethod + " threw an exception", e.getCause());
        } catch (final IllegalAccessException e) {
            throw new AssertionError(e);
        } finally {
            serviceMethod.setAccessible(false);
        }
    }
}
