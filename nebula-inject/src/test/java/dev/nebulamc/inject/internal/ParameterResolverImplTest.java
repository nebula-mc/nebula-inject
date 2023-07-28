package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.ServiceFinder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParameterResolverImplTest {

    ParameterResolver parameterResolver;

    @BeforeEach
    void setUp() {

        parameterResolver = new ParameterResolverImpl();
    }

    static class IterableParameter {

        public IterableParameter(final Iterable<String> iterable) {

        }
    }

    static class CollectionParameter {

        public CollectionParameter(final Collection<String> collection) {

        }
    }

    static class ListParameter {

        public ListParameter(final List<String> list) {

        }
    }

    static class SetParameter {

        public SetParameter(final Set<String> set) {

        }
    }

    static class ArrayParameter {

        public ArrayParameter(final String[] array) {

        }
    }

    static class GenericArrayParameter {

        public GenericArrayParameter(final List<String>[] array) {

        }
    }

    static class GenericElementParameter {

        public GenericElementParameter(final List<List<String>> list) {

        }
    }

    static class NonCollectionParameter {

        public NonCollectionParameter(final String string) {

        }
    }

    @DisplayName("resolve(Parameter, ServiceFinder)")
    @Nested
    class Resolve {

        ServiceFinder serviceFinder;

        @BeforeEach
        void setUp() {

            serviceFinder = mock();
        }

        @AfterEach
        void tearDown() {

            serviceFinder = null;
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void testResolveWhenParameterIsNull() {

            assertThrows(NullPointerException.class, () ->
                    parameterResolver.resolveParameter(null, serviceFinder));
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void testResolveWhenServiceFinderIsNull() throws NoSuchMethodException {

            final Parameter parameter = NonCollectionParameter.class
                    .getDeclaredConstructor(String.class)
                    .getParameters()[0];

            assertThrows(NullPointerException.class, () ->
                    parameterResolver.resolveParameter(parameter, null));
        }

        @Test
        void testResolveWhenParameterIsIterable() throws NoSuchMethodException {

            final Parameter parameter = IterableParameter.class
                    .getDeclaredConstructor(Iterable.class)
                    .getParameters()[0];

            when(serviceFinder.findServices(String.class))
                    .thenReturn(List.of("service 1", "service 2"));

            assertEquals(
                    List.of("service 1", "service 2"),
                    parameterResolver.resolveParameter(parameter, serviceFinder));
        }

        @Test
        void testResolveWhenParameterIsCollection() throws NoSuchMethodException {

            final Parameter parameter = CollectionParameter.class
                    .getDeclaredConstructor(Collection.class)
                    .getParameters()[0];

            when(serviceFinder.findServices(String.class))
                    .thenReturn(List.of("service 1", "service 2"));

            assertEquals(
                    List.of("service 1", "service 2"),
                    parameterResolver.resolveParameter(parameter, serviceFinder));
        }

        @Test
        void testResolveWhenParameterIsList() throws NoSuchMethodException {

            final Parameter parameter = ListParameter.class
                    .getDeclaredConstructor(List.class)
                    .getParameters()[0];

            when(serviceFinder.findServices(String.class))
                    .thenReturn(List.of("service 1", "service 2"));

            assertEquals(
                    List.of("service 1", "service 2"),
                    parameterResolver.resolveParameter(parameter, serviceFinder));
        }

        @Test
        void testResolveWhenParameterIsSet() throws NoSuchMethodException {

            final Parameter parameter = SetParameter.class
                    .getDeclaredConstructor(Set.class)
                    .getParameters()[0];

            when(serviceFinder.findServices(String.class))
                    .thenReturn(List.of("service 1", "service 2"));

            assertEquals(
                    Set.of("service 1", "service 2"),
                    parameterResolver.resolveParameter(parameter, serviceFinder));
        }

        @Test
        void testResolveWhenParameterIsArray() throws NoSuchMethodException {

            final Parameter parameter = ArrayParameter.class
                    .getDeclaredConstructor(String[].class)
                    .getParameters()[0];

            when(serviceFinder.findServices(String.class))
                    .thenReturn(List.of("service 1", "service 2"));

            assertArrayEquals(
                    new String[]{"service 1", "service 2"},
                    (String[]) parameterResolver.resolveParameter(parameter, serviceFinder));
        }

        @SuppressWarnings("rawtypes")
        @Test
        void testResolveWhenParameterIsGenericArray() throws NoSuchMethodException {

            // when the parameter is a generic array, services of the erasure of the array type
            // should be returned

            final Parameter parameter = GenericArrayParameter.class
                    .getDeclaredConstructor(List[].class)
                    .getParameters()[0];

            when(serviceFinder.findService(List[].class))
                    .thenReturn(new List[]{List.of(1, 2)});

            assertArrayEquals(
                    new List[]{List.of(1, 2)},
                    (List[]) parameterResolver.resolveParameter(parameter, serviceFinder));
        }

        @Test
        void testResolveWhenParameterIsCollectionWithGenericElementType()
                throws NoSuchMethodException {

            // when the type parameter of a collection is generic, services of the erasure of the
            // collection type should be returned (e.g. List<List<String>> -> List)

            final Parameter parameter = GenericElementParameter.class
                    .getDeclaredConstructor(List.class)
                    .getParameters()[0];

            when(serviceFinder.findService(List.class))
                    .thenReturn(List.of(List.of(1, 2)));

            assertEquals(
                    List.of(List.of(1, 2)),
                    parameterResolver.resolveParameter(parameter, serviceFinder));
        }
    }
}
