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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@NebulaInjectTest
class FactoryTest {

    @Factory IntelCpuFactory intelCpuFactory = new IntelCpuFactory();
    @Inject IntelCpu intelCpu;
    @Inject Cpu cpu;
    @Inject Computer computer;

    @Test
    void testCpuFromFactoryIsInjectedIntoTest() {

        assertEquals(intelCpu, cpu);
    }

    @Test
    void testComputerIsInjected() {

        assertEquals(cpu, computer.getCpu());
    }

    @Test
    void testParameterResolution(@Inject final Cpu cpu) {

        assertEquals(this.cpu, cpu);
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
    void testMultipleAnnotations() {

        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherFactory.create()
                .execute(LauncherDiscoveryRequestBuilder.request()
                        .selectors(DiscoverySelectors.selectClass(MultipleAnnotationsTest.class))
                        .listeners()
                        .build(), listener);

        assumeTrue(listener.getSummary().getContainersFoundCount() == 2);
        assertEquals(1, listener.getSummary().getContainersSucceededCount());
        assertEquals(1, listener.getSummary().getContainersFailedCount());
    }

    @NebulaInjectTest
    static class NonFactoryTest {

        @Factory Cpu nonFactory = new IntelCpu();

        /**
         * Required so test can fail.
         */
        @Test
        void test() {

        }
    }

    @NebulaInjectTest
    static class MultipleAnnotationsTest {

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
}
