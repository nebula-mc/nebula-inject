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
    void testNonInjectableField() {

        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherFactory.create()
                .execute(LauncherDiscoveryRequestBuilder.request()
                        .selectors(DiscoverySelectors.selectClass(NonInjectableFieldTest.class))
                        .listeners()
                        .build(), listener);

        assumeTrue(listener.getSummary().getContainersFoundCount() == 2);
        assertEquals(2, listener.getSummary().getContainersSucceededCount());
        assertEquals(1, listener.getSummary().getTotalFailureCount());
    }

    @Test
    void testNonInjectableParameter() {

        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherFactory.create()
                .execute(LauncherDiscoveryRequestBuilder.request()
                        .selectors(DiscoverySelectors.selectClass(NonInjectableParameterTest.class))
                        .listeners()
                        .build(), listener);

        assumeTrue(listener.getSummary().getContainersFoundCount() == 2);
        assertEquals(2, listener.getSummary().getContainersSucceededCount());
        assertEquals(1, listener.getSummary().getTotalFailureCount());
    }

    @NebulaInjectTest
    static class NonInjectableFieldTest {

        @Inject Cpu cpu;

        /**
         * Required so test can fail.
         */
        @Test
        void test() {

        }
    }

    @NebulaInjectTest
    static class NonInjectableParameterTest {

        @Test
        void test(@Inject final Cpu cpu) {

            System.out.println("what");
        }
    }
}
