package dev.nebulamc.inject;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * An exception thrown to indicate that a service was unable to be created.
 *
 * @author Sparky983
 * @since 0.1
 */
@NullMarked
public class ServiceException extends RuntimeException {

    /**
     * Constructs a new service exception.
     *
     * @since 0.1
     */
    public ServiceException() {

    }

    /**
     * Constructs a new service exception with the specified detail message.
     *
     * @param message the detail message
     * @since 0.1
     */
    public ServiceException(final @Nullable String message) {

        super(message);
    }

    /**
     * Constructs a new service exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     * @since 0.1
     */
    public ServiceException(final @Nullable String message, final @Nullable Throwable cause) {

        super(message, cause);
    }

    /**
     * Constructs a new service exception with the specified cause.
     *
     * @param cause the cause
     * @since 0.1
     */
    public ServiceException(final @Nullable Throwable cause) {

        super(cause);
    }
}
