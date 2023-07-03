package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

import dev.nebulamc.inject.util.Preconditions;

/**
 * A {@link ServiceDefinition} for concrete classes with an
 * <a href="package-summary.html#injectable-constructors">injectable constructor</a>.
 *
 * @author Sparky983
 * @param <T> the type of the service
 */
@NullMarked
final class InjectServiceDefinition<T> implements ServiceDefinition<T> {

    private final Class<T> serviceType;
    private final Constructor<? extends T> injectableConstructor;

    /**
     * Constructs a new {@link InjectServiceDefinition} for the given service type and
     * implementation.
     *
     * @param serviceType the type of the service
     * @param implementation the implementation of the service
     * @throws IllegalArgumentException if the implementation does not have an
     * <a href="package-summary.html#injectable-constructors">injectable constructor</a>.
     * @throws NullPointerException if the service type or implementation classes are {@code null}.
     */
    InjectServiceDefinition(final Class<T> serviceType, final Class<? extends T> implementation) {

        Preconditions.requireNonNull(serviceType, "serviceType");
        Preconditions.requireNonNull(implementation, "implementation");

        this.serviceType = serviceType;
        this.injectableConstructor = findInjectableConstructor(implementation);
    }

    @SuppressWarnings("unchecked")
    private Constructor<? extends T> findInjectableConstructor(
            final Class<? extends T> implementation) {

        assert implementation != null;

        Constructor<?> injectConstructor = null;

        final Constructor<?>[] constructors = implementation.getDeclaredConstructors();

        if (constructors.length == 1 && constructors[0].getParameterCount() == 0) {
            injectConstructor = constructors[0];
        } else {
            for (final Constructor<?> constructor : constructors) {
                if (constructor.isAnnotationPresent(Inject.class)) {
                    if (injectConstructor != null) {
                        throw new IllegalArgumentException(
                                "Multiple constructors annotated with @Inject found");
                    }
                    injectConstructor = constructor;
                }
            }
        }

        if (injectConstructor == null) {
            throw new IllegalArgumentException("No constructors annotated with @Inject" +
                    " found for type " +
                    implementation.getName());
        }

        return (Constructor<? extends T>) injectConstructor;
    }

    @Override
    public Class<T> getServiceType() {

        return serviceType;
    }

    @Override
    public T createService(final ServiceFinder serviceFinder) {

        Preconditions.requireNonNull(serviceFinder, "serviceFinder");

        final Object[] arguments = new Object[injectableConstructor.getParameterCount()];
        final Parameter[] parameters = injectableConstructor.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            final Parameter parameter = parameters[i];
            final Object service = serviceFinder.findService(parameter.getType());
            arguments[i] = service;
        }

        injectableConstructor.setAccessible(true);

        try {
            return injectableConstructor.newInstance(arguments);
        } catch (final InvocationTargetException | InstantiationException e) {
            throw new ServiceException("Constructor threw an exception", e);
        } catch (final IllegalAccessException e) {
            throw new AssertionError(e);
        } finally {
            injectableConstructor.setAccessible(false);
        }
    }
}
