package dev.nebulamc.inject.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MockitoMockFactoryTest {

    MockFactory mockFactory;

    @BeforeEach
    void setUp() {

        mockFactory = new MockitoMockFactory();
    }

    @Test
    void testCreateMock() {

        final Mock options = mock();
        when(options.answer()).thenReturn(Answers.RETURNS_MOCKS);
        final Object mock = mockFactory.createMock(Object.class, options);

        assertTrue(Mockito.mockingDetails(mock).isMock());
        assertEquals(Answers.RETURNS_MOCKS, Mockito.mockingDetails(mock)
                .getMockHandler()
                .getMockSettings()
                .getDefaultAnswer());
    }
}
