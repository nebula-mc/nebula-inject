package dev.nebulamc.inject.test;

import dev.nebulamc.inject.test.house.Heater;
import dev.nebulamc.inject.test.house.House;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NebulaInjectTestsTest {

    @Test
    void testGetMockFields() throws NoSuchFieldException {

        class MocksTest {

            @Mock Heater heater;
            @Mock House house;
        }

        final Field heater = MocksTest.class.getDeclaredField("heater");
        final Field house = MocksTest.class.getDeclaredField("house");

        final NebulaInjectTests tests = new NebulaInjectTests(MocksTest.class);

        assertEquals(Set.of(heater, house), tests.getMockFields());
    }

    @Test
    void testGetInjectMocksFields() throws NoSuchFieldException {

        class InjectMocksTest {

            @InjectMocks Heater heater;
            @InjectMocks House house;
        }

        final Field heater = InjectMocksTest.class.getDeclaredField("heater");
        final Field house = InjectMocksTest.class.getDeclaredField("house");

        final NebulaInjectTests tests = new NebulaInjectTests(InjectMocksTest.class);

        assertEquals(Set.of(heater, house), tests.getInjectMocksFields());
    }

    @Test
    void testInitWhenMockAndInjectMocks() {

        class MockAndInjectTest {

            @Mock @InjectMocks Heater heater;
        }

        assertThrows(IllegalArgumentException.class, () ->
                new NebulaInjectTests(MockAndInjectTest.class));
    }
}
