package dev.nebulamc.inject.test.computer;

import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.Service;

@Factory
public final class IntelCpuFactory {

    private final IntelCpu intelCpu;

    @Inject
    public IntelCpuFactory(final IntelCpu intelCpu) {

        this.intelCpu = intelCpu;
    }

    @Service
    public Cpu getCpu() {

        return intelCpu;
    }
}
