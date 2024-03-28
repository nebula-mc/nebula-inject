package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.Service;
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

class ServiceTest {

    @Test
    void testMultipleAnnotations() {

        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherFactory.create()
                .execute(LauncherDiscoveryRequestBuilder.request()
                        .selectors(DiscoverySelectors.selectClass(MultipleAnnotationsTest.class))
                        .listeners()
                        .build(), listener);

        assumeTrue(listener.getSummary().getContainersFoundCount() == 2);
        assertEquals(1, listener.getSummary().getContainersSucceededCount(), 1);
        assertEquals(1, listener.getSummary().getTotalFailureCount(), 1);
    }

    @Test
    void testUnassignedService() {

        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherFactory.create()
                .execute(LauncherDiscoveryRequestBuilder.request()
                        .selectors(DiscoverySelectors.selectClass(UnassignedServiceTest.class))
                        .listeners()
                        .build(), listener);

        assumeTrue(listener.getSummary().getContainersFoundCount() == 2);
        assertEquals(2, listener.getSummary().getContainersSucceededCount());
        assertEquals(1, listener.getSummary().getTotalFailureCount());
    }

    @NebulaInjectTest
    static class MultipleAnnotationsTest {

        @Service
        @Inject
        Cpu cpu = new IntelCpu();

        /**
         * Required so test can fail.
         */
        @Test
        void test() {

        }
    }

    @NebulaInjectTest
    static class UnassignedServiceTest {

        @Service Cpu cpu;

        /**
         * Required so test can fail.
         */
        @Test
        void test() {

        }
    }

    @NebulaInjectTest
    static class FieldTest {

        @Factory IntelCpuFactory intelCpuFactory = new IntelCpuFactory();
        @Service Cpu cpu = new IntelCpu();
        @Inject Computer computer;

        @Test
        void testServiceReplacesFactory(@Inject final Cpu cpu) {

            assertEquals(this.cpu, cpu);
        }

        @Test
        void testComputerIsInjected() {

            assertEquals(cpu, computer.getCpu());
        }

        @Test
        void testCpuParameterResolution(@Inject final Cpu cpu) {

            assertEquals(this.cpu, cpu);
        }
    }

    @NebulaInjectTest
    static class MethodTest {

        @Factory IntelCpuFactory intelCpuFactory = new IntelCpuFactory();
        Cpu cpu = new IntelCpu();
        @Inject Computer computer;
        @Service String string = "testing parameter resolution";

        @Test
        void testServiceReplacesFactory(@Inject final Cpu cpu) {

            assertEquals(this.cpu, cpu);
        }

        @Test
        void testComputerIsInjected() {

            assertEquals(cpu, computer.getCpu());
        }

        @Service
        Cpu createCpu(@Inject final String string) {

            assertEquals("testing parameter resolution", string);

            return cpu;
        }
    }
}
