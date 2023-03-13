package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.test.computer.Computer;
import dev.nebulamc.inject.test.computer.Cpu;
import dev.nebulamc.inject.test.computer.IntelCpu;
import dev.nebulamc.inject.test.computer.IntelCpuFactory;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@NebulaInjectTest
class FactoryTest {

    @Factory IntelCpuFactory intelCpuFactory;
    @InjectMocks IntelCpu intelCpu;
    @InjectMocks Cpu cpu;
    @InjectMocks Computer computer;

    @Test
    void testIntelCpuFactoryIsInjectedIntoFactory() {

        assertEquals(intelCpu, intelCpuFactory.getCpu());
    }

    @Test
    void testCpuFromFactoryIsInjectedIntoTest() {

        assertEquals(intelCpuFactory.getCpu(), cpu);
    }

    @Test
    void testComputerIsInjected() {

        assertEquals(cpu, computer.getCpu());
    }

    @Test
    void testNonFactory() {

        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherFactory.create()
                .execute(LauncherDiscoveryRequestBuilder.request()
                        .selectors(DiscoverySelectors.selectClass(NonFactoryTest.class))
                        .listeners()
                        .build(), listener);

        assumeTrue(listener.getSummary().getTestsFoundCount() == 1);
        assertEquals(1, listener.getSummary().getTestsFailedCount());
    }

    @Test
    void testFactoryAndMock() {

        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherFactory.create()
                .execute(LauncherDiscoveryRequestBuilder.request()
                        .selectors(DiscoverySelectors.selectClass(FactoryAndMockTest.class))
                        .listeners()
                        .build(), listener);

        assumeTrue(listener.getSummary().getContainersFoundCount() == 2);
        assertEquals(1, listener.getSummary().getContainersSucceededCount());
        assertEquals(1, listener.getSummary().getContainersFailedCount());
    }

    @Test
    void testFactoryAndInject() {

        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherFactory.create()
                .execute(LauncherDiscoveryRequestBuilder.request()
                        .selectors(DiscoverySelectors.selectClass(FactoryAndInjectTest.class))
                        .listeners()
                        .build(), listener);

        assumeTrue(listener.getSummary().getContainersFoundCount() == 2);
        assertEquals(1, listener.getSummary().getContainersSucceededCount());
        assertEquals(1, listener.getSummary().getContainersFailedCount());
    }

    @NebulaInjectTest
    static class NonFactoryTest {

        @Factory Cpu nonFactory;

        /**
         * Required so test can fail.
         */
        @Test
        void test() {

        }
    }

    @NebulaInjectTest
    static class FactoryAndMockTest {

        @Factory
        @Mock
        IntelCpuFactory factory;

        /**
         * Required so test can fail.
         */
        @Test
        void test() {

        }
    }

    @NebulaInjectTest
    static class FactoryAndInjectTest {

        @InjectMocks
        @Factory
        IntelCpuFactory factory;

        /**
         * Required so test can fail.
         */
        @Test
        void test() {

        }
    }

}
