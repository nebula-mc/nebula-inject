package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

/**
 * The definition of a service.
 *
 * @author Sparky983
 * @param <T> the type of the service
 * @since 1.0
 */
@NullMarked
public interface ServiceDefinition<T> {

    /**
     * Gets the type of the service.
     *
     * @return the type of the service
     * @since 1.0
     */
    Class<T> getServiceType();

    /**
     * Creates and performs dependency injection on a new object for this definition.
     * <p>
     * Dependencies of the service for this service definition are resolved using the specified
     * service finder.
     *
     * @param serviceFinder the service finder to use
     * @return the created object
     * @throws NoUniqueServiceException if this service finder could not find a singular service
     * required for injecting.
     * @throws NullPointerException if the service finder is {@code null}.
     * @throws ServiceException if an exception occurred while creating the service object.
     * @since 1.0
     */
    T createService(ServiceFinder serviceFinder);
}
