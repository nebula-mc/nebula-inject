package dev.nebulamc.inject.test;

import dev.nebulamc.inject.Inject;
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
    void testGetInjectFields() throws NoSuchFieldException {

        class InjectTest {

            @Inject Cpu cpu;
            @Inject Computer computer;
        }

        final Field cpu = InjectTest.class.getDeclaredField("cpu");
        final Field computer = InjectTest.class.getDeclaredField("computer");

        final NebulaInjectTests tests = new NebulaInjectTests(InjectTest.class);

        assertEquals(Set.of(cpu, computer), tests.getInjectFields());
    }

    @Test
    void testInitWhenMockAndInject() {

        class MockAndInjectTest {

            @Mock
            @Inject
            Cpu cpu;
        }

        assertThrows(IllegalArgumentException.class, () ->
                new NebulaInjectTests(MockAndInjectTest.class));
    }
}
