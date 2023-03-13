package dev.nebulamc.inject.test;

import dev.nebulamc.inject.test.computer.Computer;
import dev.nebulamc.inject.test.computer.Cpu;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NebulaInjectTestsTest {

    @Test
    void testGetMockFields() throws NoSuchFieldException {

        class MocksTest {

            @Mock Cpu cpu;
            @Mock Computer computer;
        }

        final Field cpu = MocksTest.class.getDeclaredField("cpu");
        final Field computer = MocksTest.class.getDeclaredField("computer");

        final NebulaInjectTests tests = new NebulaInjectTests(MocksTest.class);

        assertEquals(Set.of(cpu, computer), tests.getMockFields());
    }

    @Test
    void testGetInjectMocksFields() throws NoSuchFieldException {

        class InjectMocksTest {

            @InjectMocks Cpu cpu;
            @InjectMocks Computer computer;
        }

        final Field cpu = InjectMocksTest.class.getDeclaredField("cpu");
        final Field computer = InjectMocksTest.class.getDeclaredField("computer");

        final NebulaInjectTests tests = new NebulaInjectTests(InjectMocksTest.class);

        assertEquals(Set.of(cpu, computer), tests.getInjectMocksFields());
    }

    @Test
    void testInitWhenMockAndInjectMocks() {

        class MockAndInjectTest {

            @Mock @InjectMocks Cpu cpu;
        }

        assertThrows(IllegalArgumentException.class, () ->
                new NebulaInjectTests(MockAndInjectTest.class));
    }
}
