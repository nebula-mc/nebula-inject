package dev.nebulamc.inject.test.computer;

import dev.nebulamc.inject.Inject;

public final class Computer {

    private final Cpu cpu;

    @Inject
    public Computer(final Cpu cpu) {

        this.cpu = cpu;
    }

    public Cpu getCpu() {

        return cpu;
    }
}
