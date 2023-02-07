package dev.nebulamc.inject.test.house;

import dev.nebulamc.inject.Inject;

public final class House {

    private final Heater heater;

    @Inject
    public House(final Heater heater) {
        this.heater = heater;
    }

    public Heater getHeater() {
        return this.heater;
    }
}
