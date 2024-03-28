package dev.nebulamc.inject.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MockParameterResolverTest {

    MockFactory mockFactory;
    ParameterResolver parameterResolver;

    @BeforeEach
    void setUp() {

        mockFactory = mock();
        parameterResolver = new MockParameterResolver(mockFactory);
    }

    @Test
    void testSupportsParameterWhenParameterContextIsNull() {

        final ExtensionContext context = mock();

        assertThrows(NullPointerException.class, () ->
                parameterResolver.supportsParameter(null, context));
    }

    @Test
    void testSupportsParameterWhenParameterIsNull() {

        final ExtensionContext context = mock();

        assertThrows(NullPointerException.class, () ->
                parameterResolver.supportsParameter(null, context));
    }

    @Test
    void testSupportsParameterWhenParameterIsNotAnnotated() {

        final ExtensionContext context = mock();
        final ParameterContext parameterContext = mock();

        when(parameterContext.isAnnotated(Mock.class)).thenReturn(false);

        assertFalse(parameterResolver.supportsParameter(parameterContext, context));
    }

    @Test
    void testSupportsParameterWhenParameterIsAnnotated() {

        final ExtensionContext context = mock();
        final ParameterContext parameterContext = mock();

        when(parameterContext.isAnnotated(Mock.class)).thenReturn(true);

        assertTrue(parameterResolver.supportsParameter(parameterContext, context));
    }

    @Test
    void testResolveParameterWhenParameterContextIsNull() {

        final ExtensionContext context = mock();

        assertThrows(NullPointerException.class, () ->
                parameterResolver.resolveParameter(null, context));
    }

    @Test
    void testResolveParameterWhenExtensionContextIsNull() {

        final ParameterContext parameterContext = mock();

        assertThrows(NullPointerException.class, () ->
                parameterResolver.resolveParameter(parameterContext, null));
    }

    @Test
    void testResolveParameterWhenParameterIsNotAnnotated() {

        final ExtensionContext context = mock();
        final ParameterContext parameterContext = mock();

        when(parameterContext.findAnnotation(Mock.class)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                parameterResolver.resolveParameter(parameterContext, context));
    }

    @Test
    void testResolveParameterWhenParameterIsAnnotated() throws NoSuchMethodException {

        // Parameters aren't mockable
        interface TestParameter {

            void parameter(Object parameter);
        }

        final Parameter parameter = TestParameter.class
                .getDeclaredMethod("parameter", Object.class)
                .getParameters()[0];
        final Object mock = new Object();
        final ExtensionContext context = mock();
        final ParameterContext parameterContext = mock();
        final Mock options = mock();
        when(parameterContext.findAnnotation(Mock.class)).thenReturn(Optional.of(options));
        when(parameterContext.getParameter()).thenReturn(parameter);
        when(mockFactory.createMock(Object.class, options)).thenAnswer((invocation) -> mock);

        // This test is a bit of a hack, but it's the best I can do

        final Object argument = parameterResolver.resolveParameter(parameterContext, context);

        assertEquals(mock, argument);
    }
}
