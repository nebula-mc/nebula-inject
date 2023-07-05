package dev.nebulamc.inject.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PreconditionsTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    void testRequireNonNullWhenObjectIsNull() {

        assertThrows(NullPointerException.class, () -> Preconditions.requireNonNull(null, "test"));
    }

    @Test
    void testRequireNonNullWhenObjectIsNonNull() {

        assertDoesNotThrow(() -> Preconditions.requireNonNull(new Object(), "test"));
    }
}
