package dev.nebulamc.inject.test;

import org.jspecify.nullness.NullMarked;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a test class that is annotated with {@link NebulaInjectTest}.
 *
 * @author Sparky983
 */
@NullMarked
final class NebulaInjectTests {

    private final Set<Field> mockFields;
    private final Set<Field> injectMocksFields;

    NebulaInjectTests(final Class<?> testClass) {

        Objects.requireNonNull(testClass, "testClass cannot be null");

        final Field[] fields = testClass.getDeclaredFields();

        this.mockFields = Arrays.stream(fields)
                .filter((field) -> field.isAnnotationPresent(Mock.class))
                .collect(Collectors.toSet());
        this.injectMocksFields = Arrays.stream(fields)
                .filter((field) -> field.isAnnotationPresent(InjectMocks.class))
                .collect(Collectors.toSet());

        final Set<Field> mockAndInjectMockFields = new HashSet<>(mockFields);
        mockAndInjectMockFields.retainAll(injectMocksFields);

        if (!mockAndInjectMockFields.isEmpty()) {
            throw new IllegalArgumentException(
                    "The following fields cannot be annotated with both @Mock and @InjectMocks: " +
                            mockAndInjectMockFields.stream()
                                    .map(Field::getName)
                                    .collect(Collectors.joining(", ")));
        }
    }

    public Set<Field> getMockFields() {

        return mockFields;
    }

    public Set<Field> getInjectMocksFields() {

        return injectMocksFields;
    }
}
