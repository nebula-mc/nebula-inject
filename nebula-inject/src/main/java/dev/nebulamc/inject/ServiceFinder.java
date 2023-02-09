package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

import java.util.List;
import java.util.Optional;

/**
 * Responsible for finding services.
 *
 * @author Sparky983
 * @since 1.0
 */
@NullMarked
public interface ServiceFinder {

    /**
     * Finds a service of the specified type.
     *
     * @param serviceType the type of the service
     * @return the found service
     * @param <T> the type of the service
     * @throws NoUniqueServiceException if zero or multiple service was found.
     * @throws NullPointerException if the service is {@code null}.
     * @throws ServiceException if an exception occurred while finding the service object.
     * @since 1.0
     */
    <T> T findService(Class<T> serviceType);

    /**
     * Finds a service of the specified type.
     *
     * @param serviceType the type of the service
     * @return the found service or {@link Optional#empty()} if zero or multiple were found
     * @param <T> the type of the service
     * @throws NullPointerException if the service is {@code null}.
     * @throws ServiceException if an exception occurred while finding the service object.
     * @since 1.0
     */
    <T> Optional<T> findOptionalService(Class<T> serviceType);

    /**
     * Finds all service of the specified type.
     *
     * @param serviceType the type of the service
     * @return an immutable list of all found services or {@link List#of()} if none were found
     * @param <T> the type of the service
     * @throws NullPointerException if the service is {@code null}.
     * @throws ServiceException if an exception occurred while finding the service objects.
     * @since 1.0
     */
    <T> List<T> findServices(Class<T> serviceType);
}
