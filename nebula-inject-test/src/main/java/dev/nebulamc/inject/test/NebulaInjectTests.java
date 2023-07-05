package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.Service;
import dev.nebulamc.inject.util.Preconditions;
import org.jspecify.nullness.NullMarked;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final Set<Field> serviceFields;

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
        this.serviceFields = Arrays.stream(fields)
                .filter((field) -> field.isAnnotationPresent(Service.class))
                .collect(Collectors.toSet());

        final Set<Field> annotatedFields = Stream.concat(
                        Stream.concat(mockFields.stream(), injectFields.stream()),
                        Stream.concat(factoryFields.stream(), serviceFields.stream()))
                .collect(Collectors.toSet());

        if (annotatedFields.size() != mockFields.size() +
                injectFields.size() +
                factoryFields.size() +
                serviceFields.size()) {
            throw new IllegalArgumentException(
                    "Fields must have only one of the following annotations: " +
                            "@Mock, @Inject, @Factory, @Service");
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

    public Set<Field> getServiceFields() {

        return serviceFields;
    }
}
