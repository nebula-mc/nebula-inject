package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

/**
 * An internal utility class for checking preconditions.
 *
 * @author Sparky983
 */
@NullMarked
final class Preconditions {

    private Preconditions() {

    }

    /**
     * Checks that a parameter is not {@code null} with a customized message.
     * <p>
     * Intended to be used to prevent null-pollution, not to make null-assertions.
     *
     * @param object the object to check for {@code null}
     * @param name the name of the object
     * @throws NullPointerException if the object is {@code null}.
     */
    @SuppressWarnings("ConstantConditions")
    static void requireNonNull(final Object object, final String name) {

        /*
         The object parameter is @NonNull because this method should be only used to prevent
         null-pollution at runtime. Checking a @Nullable Object would be an invalid usage of this
         method.

         Forcing this method to be void prevents both using this method as a null-assertion and
         forgetting to call this method by not being able to use its return value later.
         */

        if (object == null) {
            throw new NullPointerException("Argument \"" + name + "\" cannot be null");
        }
    }
}
