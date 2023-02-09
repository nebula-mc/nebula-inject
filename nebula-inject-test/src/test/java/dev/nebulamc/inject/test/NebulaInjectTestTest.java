package dev.nebulamc.inject.test;

import dev.nebulamc.inject.test.house.Heater;
import dev.nebulamc.inject.test.house.House;
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
class NebulaInjectTestTest {

    @Mock(answer = Answers.RETURNS_MOCKS) Heater heater;
    @InjectMocks House house;

    @Test
    void testHeaterFieldIsInjected() {

        assertEquals(heater, house.getHeater());
    }

    @Test
    void testHeaterFieldIsMocked() {

        assertTrue(Mockito.mockingDetails(heater).isMock());
        assertEquals(Answers.RETURNS_MOCKS, Mockito.mockingDetails(heater)
                .getMockHandler()
                .getMockSettings()
                .getDefaultAnswer());
    }

    @Test
    void testHeaterIsMocked(@Mock(answer = Answers.RETURNS_SMART_NULLS) final Heater heater) {

        assertTrue(Mockito.mockingDetails(heater).isMock());
        assertEquals(Answers.RETURNS_SMART_NULLS, Mockito.mockingDetails(heater)
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
        assumeTrue(listener.getSummary().getContainersSucceededCount() == 1);
        assertEquals(listener.getSummary().getTotalFailureCount(), 1);
    }

    @NebulaInjectTest
    static class InjectAndMockFieldTest {

        @Mock @InjectMocks House house;

        /**
         * Required because the test container can't fail, only tests can.
         */
        @Test
        void test() {}
    }
}
