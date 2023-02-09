package dev.nebulamc.inject.test;

import dev.nebulamc.inject.test.house.Heater;
import dev.nebulamc.inject.test.house.House;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Answers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void testBeforeEach() throws Exception {

        class Test {

            @Mock(answer = Answers.RETURNS_MOCKS) Heater heater;
            @InjectMocks House house;
        }

        final Test test = new Test();

        final Heater heater = mock();
        final ExtensionContext context = mock(ExtensionContext.class);
        when(context.getRequiredTestClass()).thenAnswer((invocation) -> Test.class);
        when(context.getRequiredTestInstance()).thenReturn(test);
        when(mockFactory.createMock(
                eq(Heater.class),
                argThat((argument) -> argument.answer() == Answers.RETURNS_MOCKS)))
                .thenReturn(heater);
        containerExtension.beforeAll(context);

        containerExtension.beforeEach(context);

        assertEquals(heater, test.heater);
        assertEquals(heater, test.house.getHeater());
    }
}