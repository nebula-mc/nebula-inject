package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.Service;
import dev.nebulamc.inject.test.computer.Computer;
import dev.nebulamc.inject.test.computer.Cpu;
import dev.nebulamc.inject.test.computer.IntelCpu;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@NebulaInjectTest
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
        assertEquals(listener.getSummary().getContainersSucceededCount(), 1);
        assertEquals(listener.getSummary().getTotalFailureCount(), 1);
    }

    @NebulaInjectTest
    static class MultipleAnnotationsTest {

        @Service
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
    @Nested
    class FieldTest {

        @Service Cpu cpu = new IntelCpu();
        @Inject Computer computer;

        @Test
        void testComputerIsInjected() {

            assertEquals(cpu, computer.getCpu());
        }
    }

    @NebulaInjectTest
    @Nested
    class MethodTest {

        Cpu cpu = new IntelCpu();
        @Inject Computer computer;

        @Test
        void testComputerIsInjected() {

            assertEquals(cpu, computer.getCpu());
        }

        @Service
        Cpu createCpu(@Mock final Computer computer) {

            assertTrue(Mockito.mockingDetails(computer).isMock());

            return cpu;
        }
    }
}
