package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.test.computer.Computer;
import dev.nebulamc.inject.test.computer.Cpu;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.mockito.Answers;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@NebulaInjectTest
class MockTest {

    @Mock(answer = Answers.RETURNS_MOCKS) Cpu cpu;
    @Inject Computer computer;

    @Test
    void testComputerIsInjected() {

        assertEquals(cpu, computer.getCpu());
    }

    @Test
    void testCpuFieldIsMocked() {

        assertTrue(Mockito.mockingDetails(cpu).isMock());
        assertEquals(Answers.RETURNS_MOCKS, Mockito.mockingDetails(cpu)
                .getMockHandler()
                .getMockSettings()
                .getDefaultAnswer());
    }

    @Test
    void testCpuIsMocked(@Mock(answer = Answers.RETURNS_SMART_NULLS) final Cpu cpu) {

        assertTrue(Mockito.mockingDetails(cpu).isMock());
        assertEquals(Answers.RETURNS_SMART_NULLS, Mockito.mockingDetails(cpu)
                .getMockHandler()
                .getMockSettings()
                .getDefaultAnswer());
    }

    @Test
    void testInjectAndMockField() {

        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherFactory.create()
                .execute(LauncherDiscoveryRequestBuilder.request()
                        .selectors(DiscoverySelectors.selectClass(InjectAndMockFieldTest.class))
                        .listeners()
                        .build(), listener);

        assumeTrue(listener.getSummary().getContainersFoundCount() == 2);
        assertEquals(listener.getSummary().getContainersSucceededCount(), 1);
        assertEquals(listener.getSummary().getTotalFailureCount(), 1);
    }

    @NebulaInjectTest
    static class InjectAndMockFieldTest {

        @Mock
        @Inject
        Computer computer;

        /**
         * Required because the test container can't fail, only tests can.
         */
        @Test
        void test() {

        }
    }
}
