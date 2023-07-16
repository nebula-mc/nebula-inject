package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.Service;
import dev.nebulamc.inject.internal.util.Preconditions;
import org.jspecify.nullness.NullMarked;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
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
    private final Set<Method> serviceMethods;

    NebulaInjectTests(final Class<?> testClass) {

        Preconditions.requireNonNull(testClass, "testClass");

        final Field[] fields = testClass.getDeclaredFields();

        this.mockFields = Collections.unmodifiableSet(Arrays.stream(fields)
                .filter((field) -> field.isAnnotationPresent(Mock.class))
                .collect(Collectors.<Field, Set<Field>>toCollection(LinkedHashSet::new)));
        this.injectFields = Collections.unmodifiableSet(Arrays.stream(fields)
                .filter((field) -> field.isAnnotationPresent(Inject.class))
                .collect(Collectors.<Field, Set<Field>>toCollection(LinkedHashSet::new)));
        this.factoryFields = Collections.unmodifiableSet(Arrays.stream(fields)
                .filter((field) -> field.isAnnotationPresent(Factory.class))
                .collect(Collectors.<Field, Set<Field>>toCollection(LinkedHashSet::new)));
        this.serviceFields = Collections.unmodifiableSet(Arrays.stream(fields)
                .filter((field) -> field.isAnnotationPresent(Service.class))
                .collect(Collectors.<Field, Set<Field>>toCollection(LinkedHashSet::new)));
        this.serviceMethods = Collections.unmodifiableSet(Arrays.stream(testClass.getDeclaredMethods())
                .filter((method) -> method.isAnnotationPresent(Service.class))
                .collect(Collectors.<Method, Set<Method>>toCollection(LinkedHashSet::new)));

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

    public Set<Method> getServiceMethods() {

        return serviceMethods;
    }

}
