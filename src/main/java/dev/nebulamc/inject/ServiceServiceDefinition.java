package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@NullMarked
final class ServiceServiceDefinition<T> implements ServiceDefinition<T> {

    private final Object factory;
    private final Method serviceMethod;
    /**
     * Cache the {@link #serviceMethod} metadata, so we don't have to load it if we just want the
     * service type.
     */
    private final Class<T> serviceType;
    /**
     * Cached method parameters.
     */
    private final Parameter[] parameters;

    /**
     * Constructs a new {@link ServiceServiceDefinition} for the specified {@link Service} method.
     *
     * @param factory the factory object
     * @param serviceMethod the service serviceMethod
     * @throws ClassCastException if the service method is not a member of the factory's class.
     * @throws IllegalArgumentException if the service method is not public.
     * @throws NullPointerException if the factory or service method are {@code null}.
     */
    @SuppressWarnings("unchecked")
    ServiceServiceDefinition(final Object factory, final Method serviceMethod) {

        Preconditions.requireNonNull(factory, "factory");
        Preconditions.requireNonNull(serviceMethod, "serviceMethod");

        if (!serviceMethod.getDeclaringClass().isInstance(factory)) {
            throw new ClassCastException(
                    "The service method is not a member of the factory's class.");
        }

        this.factory = factory;
        this.serviceMethod = serviceMethod;
        this.serviceType = (Class<T>) serviceMethod.getReturnType();
        this.parameters = serviceMethod.getParameters();
    }

    @Override
    public Class<T> getServiceType() {

        return serviceType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T createObject(final ServiceFinder serviceFinder) {

        Preconditions.requireNonNull(serviceFinder, "serviceFinder");

        final Object[] arguments = new Object[parameters.length];

        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = serviceFinder.findService(parameters[i].getType());
        }

        try {
            return (T) serviceMethod.invoke(factory, arguments);
        } catch (final InvocationTargetException e) {
            throw new ServiceException(e);
        } catch (final IllegalAccessException e) {
            throw new ServiceException("The service method was not accessible", e);
        }
    }
}