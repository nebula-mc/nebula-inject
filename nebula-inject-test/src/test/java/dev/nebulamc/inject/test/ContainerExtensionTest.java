package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.Service;
import dev.nebulamc.inject.test.computer.Computer;
import dev.nebulamc.inject.test.computer.Cpu;
import dev.nebulamc.inject.test.computer.IntelCpu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.mockito.Answers;

import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContainerExtensionTest {

    MockFactory mockFactory;
    ContainerExtension containerExtension;

    @BeforeEach
    void beforeEach() {

        mockFactory = mock(MockFactory.class);
        containerExtension = new ContainerExtension(mockFactory);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testInitWhenMockFactoryIsNull() {

        assertThrows(NullPointerException.class, () -> new ContainerExtension(null));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testBeforeAllWhenContextIsNull() {

        assertThrows(NullPointerException.class, () -> containerExtension.beforeAll(null));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testBeforeEachWhenContextIsNull() {

        assertThrows(NullPointerException.class, () -> containerExtension.beforeEach(null));
    }

    @Test
    void testBeforeEachWhenBeforeAllNotCalled() {

        final ExtensionContext context = mock(ExtensionContext.class);

        assertThrows(IllegalStateException.class, () -> containerExtension.beforeEach(context));
    }

    @Test
    void testBeforeEachWhenBeforeEachAlreadyCalled() throws Exception {

        class Test {

        }

        final ExtensionContext context = mock(ExtensionContext.class);
        when(context.getRequiredTestClass()).thenAnswer((invocation) -> Test.class);

        containerExtension.beforeAll(context);
        containerExtension.beforeEach(context);

        assertThrows(IllegalStateException.class, () -> containerExtension.beforeEach(context));
    }

    @Test
    void testBeforeEach() throws Exception {

        class Test {

            @Mock(answer = Answers.RETURNS_MOCKS) Cpu cpu;
            @Inject Computer computer;
        }

        final Test test = new Test();

        final Cpu cpu = mock();
        final ExtensionContext context = mock(ExtensionContext.class);
        when(context.getRequiredTestClass()).thenAnswer((invocation) -> Test.class);
        when(context.getRequiredTestInstance()).thenReturn(test);
        when(mockFactory.createMock(
                eq(Cpu.class),
                argThat((argument) -> argument.answer() == Answers.RETURNS_MOCKS)))
                .thenReturn(cpu);
        containerExtension.beforeAll(context);

        containerExtension.beforeEach(context);

        assertEquals(cpu, test.cpu);
        assertEquals(cpu, test.computer.getCpu());
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAfterEachWhenContextIsNull() {

        assertThrows(NullPointerException.class, () -> containerExtension.afterEach(null));
    }

    @Test
    void testAfterEachWhenBeforeAllNotCalled() {

        final ExtensionContext context = mock(ExtensionContext.class);

        assertThrows(IllegalStateException.class, () -> containerExtension.afterEach(context));
    }

    @Test
    void testAfterEachWhenBeforeEachNotCalled() {

        class Test {

        }

        final ExtensionContext context = mock(ExtensionContext.class);
        when(context.getRequiredTestClass()).thenAnswer((invocation) -> Test.class);

        containerExtension.beforeAll(context);

        assertThrows(IllegalStateException.class, () -> containerExtension.afterEach(context));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testSupportsParameterWhenParameterContextIsNull() {

        final ExtensionContext context = mock(ExtensionContext.class);

        assertThrows(
                NullPointerException.class,
                () -> containerExtension.supportsParameter(null, context));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testSupportsParameterWhenContextIsNull() {

        final ParameterContext context = mock(ParameterContext.class);

        assertThrows(
                NullPointerException.class,
                () -> containerExtension.supportsParameter(context, null));
    }

    @Test
    void testSupportsParameterWhenParameterIsNotAnnotated() {

        final ParameterContext parameterContext = mock(ParameterContext.class);
        final ExtensionContext extensionContext = mock(ExtensionContext.class);

        when(parameterContext.isAnnotated(Inject.class)).thenReturn(false);

        assertFalse(containerExtension.supportsParameter(parameterContext, extensionContext));
    }

    @Test
    void testSupportsParameterWhenParameterIsAnnotated() {

        final ParameterContext parameterContext = mock(ParameterContext.class);
        final ExtensionContext extensionContext = mock(ExtensionContext.class);

        when(parameterContext.isAnnotated(Inject.class)).thenReturn(true);

        assertTrue(containerExtension.supportsParameter(parameterContext, extensionContext));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testResolveParameterWhenParameterContextIsNull() {

        final ExtensionContext extensionContext = mock(ExtensionContext.class);

        assertThrows(
                NullPointerException.class,
                () -> containerExtension.supportsParameter(null, extensionContext));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testResolveParameterWhenContextIsNull() {

        final ParameterContext parameterContext = mock(ParameterContext.class);

        assertThrows(
                NullPointerException.class,
                () -> containerExtension.supportsParameter(parameterContext, null));
    }

    @Test
    void testResolveParameterWhenBeforeAllIsNotCalled() {

        final ParameterContext parameterContext = mock(ParameterContext.class);
        final ExtensionContext extensionContext = mock(ExtensionContext.class);

        assertThrows(
                IllegalStateException.class,
                () -> containerExtension.resolveParameter(parameterContext, extensionContext));
    }

    @Test
    void testResolveParameterWhenBeforeEachIsNotCalled() {

        final ParameterContext parameterContext = mock(ParameterContext.class);
        final ExtensionContext extensionContext = mock(ExtensionContext.class);

        class Test {

        }

        when(extensionContext.getRequiredTestInstance()).thenReturn(new Test());
        when(extensionContext.getRequiredTestClass()).thenAnswer((invocation) -> Test.class);

        containerExtension.beforeAll(extensionContext);

        assertThrows(
                IllegalStateException.class,
                () -> containerExtension.resolveParameter(parameterContext, extensionContext));
    }

    @Test
    void testResolveParameterWhenParameterIsNotAnnotated() throws Exception {

        final ParameterContext parameterContext = mock(ParameterContext.class);

        when(parameterContext.isAnnotated(Inject.class)).thenReturn(false);

        class Test {

        }

        final ExtensionContext extensionContext = mock(ExtensionContext.class);
        when(extensionContext.getRequiredTestInstance()).thenReturn(new Test());
        when(extensionContext.getRequiredTestClass()).thenAnswer((invocation) -> Test.class);

        containerExtension.beforeAll(extensionContext);
        containerExtension.beforeEach(extensionContext);

        assertThrows(
                IllegalArgumentException.class,
                () -> containerExtension.resolveParameter(parameterContext, extensionContext));
    }

    @Test
    void testResolveParameterWhenParameterIsAnnotated() throws Exception {

        // Parameters aren't mockable
        interface TestParameter {

            void parameter(@Inject Cpu cpu);
        }

        final Parameter parameter = TestParameter.class
                .getDeclaredMethod("parameter", Cpu.class)
                .getParameters()[0];

        final ParameterContext parameterContext = mock(ParameterContext.class);
        when(parameterContext.isAnnotated(Inject.class)).thenReturn(true);
        when(parameterContext.getParameter()).thenReturn(parameter);

        final ExtensionContext extensionContext = mock(ExtensionContext.class);
        final IntelCpu intelCpu = new IntelCpu();

        class Test {

            final @Service Cpu cpu = intelCpu;
        }

        when(extensionContext.getRequiredTestInstance()).thenReturn(new Test());
        when(extensionContext.getRequiredTestClass()).thenAnswer((mock) -> Test.class);
        containerExtension.beforeAll(extensionContext);
        containerExtension.beforeEach(extensionContext);

        final Object argument =
                containerExtension.resolveParameter(parameterContext, extensionContext);

        assertEquals(intelCpu, argument);
    }

    @Test
    void testResolveParameterWhenParameterIsNotInjectable() throws Exception {

        // Parameters aren't mockable
        interface TestParameter {

            void parameter(@Inject Cpu cpu);
        }

        final Parameter parameter = TestParameter.class
                .getDeclaredMethod("parameter", Cpu.class)
                .getParameters()[0];

        final ParameterContext parameterContext = mock(ParameterContext.class);
        when(parameterContext.isAnnotated(Inject.class)).thenReturn(true);
        when(parameterContext.getParameter()).thenReturn(parameter);

        final ExtensionContext extensionContext = mock(ExtensionContext.class);
        final IntelCpu intelCpu = new IntelCpu();

        class Test {

        }

        when(extensionContext.getRequiredTestInstance()).thenReturn(new Test());
        when(extensionContext.getRequiredTestClass()).thenAnswer((mock) -> Test.class);
        containerExtension.beforeAll(extensionContext);
        containerExtension.beforeEach(extensionContext);

        assertThrows(
                ParameterResolutionException.class,
                () -> containerExtension.resolveParameter(parameterContext, extensionContext));
    }
}
