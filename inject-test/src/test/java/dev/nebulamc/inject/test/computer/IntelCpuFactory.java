package dev.nebulamc.inject.test.computer;

import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Inject;
import dev.nebulamc.inject.Service;

@Factory
public final class IntelCpuFactory {

    @Service
    public Cpu getCpu(final IntelCpu cpu) {

        return cpu;
    }
}
