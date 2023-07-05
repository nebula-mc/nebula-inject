package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * An exception thrown to indicate multiple or no services were found for a given type.
 *
 * @author Sparky983
 * @since 0.1
 */
@NullMarked
public class NoUniqueServiceException extends RuntimeException {

    /**
     * Constructs a new no unique service exception.
     *
     * @since 0.1
     */
    public NoUniqueServiceException() {

    }

    /**
     * Constructs a new no unique service exception with the specified detail message.
     *
     * @param message the detail message
     * @since 0.1
     */
    public NoUniqueServiceException(final @Nullable String message) {

        super(message);
    }

    /**
     * Constructs a new no unique service exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     * @since 0.1
     */
    public NoUniqueServiceException(final @Nullable String message,
                                    final @Nullable Throwable cause) {

        super(message, cause);
    }

    /**
     * Constructs a new no unique service exception with the specified cause.
     *
     * @param cause the cause
     * @since 0.1
     */
    public NoUniqueServiceException(final @Nullable Throwable cause) {

        super(cause);
    }
}
