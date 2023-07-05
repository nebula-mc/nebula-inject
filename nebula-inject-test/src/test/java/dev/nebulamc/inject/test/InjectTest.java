package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.test.computer.Cpu;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class InjectTest {

    @Test
    void testNonInjectableParameter() {

        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherFactory.create()
                .execute(LauncherDiscoveryRequestBuilder.request()
                        .selectors(DiscoverySelectors.selectClass(NonInjectableParameterTest.class))
                        .listeners()
                        .build(), listener);

        assumeTrue(listener.getSummary().getContainersFoundCount() == 2);
        assertEquals(listener.getSummary().getContainersSucceededCount(), 2);
        assertEquals(listener.getSummary().getTotalFailureCount(), 1);
    }

    @NebulaInjectTest
    static class NonInjectableParameterTest {

        @Test
        void test(@Inject final Cpu cpu) {

            System.out.println("what");
        }
    }
}
