package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.util.Preconditions;
import org.jspecify.nullness.NullMarked;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
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
    private final Set<Field> injectFields;
    private final Set<Field> factoryFields;

    NebulaInjectTests(final Class<?> testClass) {

        Preconditions.requireNonNull(testClass, "testClass");

        // TODO: Make a class that performs all of this in compliance with the open-closed principle

        final Field[] fields = testClass.getDeclaredFields();

        this.mockFields = Arrays.stream(fields)
                .filter((field) -> field.isAnnotationPresent(Mock.class))
                .collect(Collectors.toSet());
        this.injectFields = Arrays.stream(fields)
                .filter((field) -> field.isAnnotationPresent(Inject.class))
                .collect(Collectors.toSet());
        this.factoryFields = Arrays.stream(fields)
                .filter((field) -> field.isAnnotationPresent(Factory.class))
                .collect(Collectors.toSet());

        final Set<Field> mockAndInjectFields = new HashSet<>(mockFields);
        mockAndInjectFields.retainAll(injectFields);

        if (!mockAndInjectFields.isEmpty()) {
            throw new IllegalArgumentException(
                    "The following fields cannot be annotated with both @Mock and @Inject: " +
                            mockAndInjectFields.stream()
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

        final Set<Field> injectAndFactoryFields = new HashSet<>(injectFields);
        injectAndFactoryFields.retainAll(factoryFields);

        if (!injectAndFactoryFields.isEmpty()) {
            throw new IllegalArgumentException(
                    "The following fields cannot be annotated with both @Inject and @Factory: " +
                            injectAndFactoryFields.stream()
                                    .map(Field::getName)
                                    .collect(Collectors.joining(", ")));
        }
    }

    public Set<Field> getMockFields() {

        return mockFields;
    }

    public Set<Field> getInjectFields() {

        return injectFields;
    }

    public Set<Field> getFactoryFields() {

        return factoryFields;
    }
}
