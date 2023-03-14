package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Factory;
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
    private final Set<Field> factoryFields;

    NebulaInjectTests(final Class<?> testClass) {

        Objects.requireNonNull(testClass, "testClass cannot be null");

        // TODO: Make a class that performs all of this in compliance with the open-closed principle

        final Field[] fields = testClass.getDeclaredFields();

        this.mockFields = Arrays.stream(fields)
                .filter((field) -> field.isAnnotationPresent(Mock.class))
                .collect(Collectors.toSet());
        this.injectMocksFields = Arrays.stream(fields)
                .filter((field) -> field.isAnnotationPresent(InjectMocks.class))
                .collect(Collectors.toSet());
        this.factoryFields = Arrays.stream(fields)
                .filter((field) -> field.isAnnotationPresent(Factory.class))
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

        final Set<Field> mockAndFactoryFields = new HashSet<>(mockFields);
        mockAndFactoryFields.retainAll(factoryFields);

        if (!mockAndFactoryFields.isEmpty()) {
            throw new IllegalArgumentException(
                    "The following fields cannot be annotated with both @Mock and @Factory: " +
                            mockAndFactoryFields.stream()
                                    .map(Field::getName)
                                    .collect(Collectors.joining(", ")));
        }

        final Set<Field> injectMocksAndFactoryFields = new HashSet<>(injectMocksFields);
        injectMocksAndFactoryFields.retainAll(factoryFields);

        if (!injectMocksAndFactoryFields.isEmpty()) {
            throw new IllegalArgumentException(
                    "The following fields cannot be annotated with both @InjectMocks and @Factory: " +
                            injectMocksAndFactoryFields.stream()
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

    public Set<Field> getFactoryFields() {

        return factoryFields;
    }
}
