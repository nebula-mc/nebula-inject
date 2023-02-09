package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * An exception thrown to indicate that a service was unable to be created.
 *
 * @author Sparky983
 * @since 1.0
 */
@NullMarked
public class ServiceException extends RuntimeException {

    /**
     * Constructs a new service exception.
     *
     * @since 1.0
     */
    public ServiceException() {

    }

    /**
     * Constructs a new service exception with the specified detail message.
     *
     * @param message the detail message
     * @since 1.0
     */
    public ServiceException(final @Nullable String message) {

        super(message);
    }

    /**
     * Constructs a new service exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     * @since 1.0
     */
    public ServiceException(final @Nullable String message, final @Nullable Throwable cause) {

        super(message, cause);
    }

    /**
     * Constructs a new service exception with the specified cause.
     *
     * @param cause the cause
     * @since 1.0
     */
    public ServiceException(final @Nullable Throwable cause) {

        super(cause);
    }
}
