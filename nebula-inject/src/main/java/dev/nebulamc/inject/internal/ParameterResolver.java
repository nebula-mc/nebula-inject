package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.NoUniqueServiceException;
import dev.nebulamc.inject.ServiceException;
import dev.nebulamc.inject.ServiceFinder;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Parameter;

/**
 * Resolves {@link Parameter} instances to a value.
 *
 * @author Sparky983
 */
@NullMarked
public interface ParameterResolver {

    /**
     * Resolves the given {@link Parameter} to a value.
     *
     * @param parameter the parameter to resolve
     * @param serviceFinder the service finder to use
     * @return the resolved value
     * @throws NoUniqueServiceException if this parameter resolver could not find a singular service
     * required for injecting.
     * @throws NullPointerException if the parameter or service finder are {@code null}.
     * @throws ServiceException if an exception occurred while resolving the parameter.
     */
    Object resolveParameter(Parameter parameter, ServiceFinder serviceFinder);
}
