package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.test.computer.Computer;
import dev.nebulamc.inject.test.computer.Cpu;
import dev.nebulamc.inject.test.computer.IntelCpu;
import dev.nebulamc.inject.test.computer.IntelCpuFactory;
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

class MockTest {

    @Test
    void testMultipleAnnotations() {

        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherFactory.create()
                .execute(LauncherDiscoveryRequestBuilder.request()
                        .selectors(DiscoverySelectors.selectClass(MultipleAnnotationsTest.class))
                        .listeners()
                        .build(), listener);

        assumeTrue(listener.getSummary().getContainersFoundCount() == 2);
        assertEquals(listener.getSummary().getContainersSucceededCount(), 1);
        assertEquals(listener.getSummary().getTotalFailureCount(), 1);
    }

    @Test
    void testInjectMockSubtype() {

        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherFactory.create()
                .execute(LauncherDiscoveryRequestBuilder.request()
                        .selectors(DiscoverySelectors.selectClass(InjectMockSubtypeTest.class))
                        .listeners()
                        .build(), listener);

        assumeTrue(listener.getSummary().getTestsFoundCount() == 1);
        assertEquals(1, listener.getSummary().getTestsFailedCount());
    }

    @NebulaInjectTest
    static class MultipleAnnotationsTest {

        @Mock
        @Inject
        Computer computer;

        /**
         * Required so test can fail.
         */
        @Test
        void test() {

        }
    }

    @NebulaInjectTest
    static class InjectMockSubtypeTest {

        @Mock IntelCpu intelCpu;
        @Inject Cpu cpu;

        /**
         * Required so test can fail.
         */
        @Test
        void test() {

        }
    }

    @NebulaInjectTest
    static class MockFieldTest {

        @Factory IntelCpuFactory intelCpuFactory = new IntelCpuFactory();
        @Mock(answer = Answers.RETURNS_MOCKS) Cpu cpu;
        @Inject Computer computer;

        @Test
        void testMockedIntelCpuReplacesFactory(@Inject final IntelCpu intelCpu) {

            assertTrue(Mockito.mockingDetails(cpu).isMock());
        }

        @Test
        void testComputerIsInjected() {

            assertEquals(cpu, computer.getCpu());
        }

        @Test
        void testCpuIsMocked() {

            assertTrue(Mockito.mockingDetails(cpu).isMock());
            assertEquals(Answers.RETURNS_MOCKS, Mockito.mockingDetails(cpu)
                    .getMockHandler()
                    .getMockSettings()
                    .getDefaultAnswer());
        }

        @Test
        void testParameterResolution(@Inject final Cpu cpu) {

            assertEquals(this.cpu, cpu);
        }
    }

    @NebulaInjectTest
    static class MockParameterTest {

        @Test
        void testCpuIsMocked(@Mock(answer = Answers.RETURNS_SMART_NULLS) final Cpu cpu) {

            assertTrue(Mockito.mockingDetails(cpu).isMock());
            assertEquals(Answers.RETURNS_SMART_NULLS, Mockito.mockingDetails(cpu)
                    .getMockHandler()
                    .getMockSettings()
                    .getDefaultAnswer());
        }
    }
}
