package dev.nebulamc.inject.car;

import dev.nebulamc.inject.Inject;

public final class Sedan implements Car {

    private final Engine engine;
    private final Wheels wheels;

    @Inject
    private Sedan(final Engine engine, final Wheels wheels) {

        this.engine = engine;
        this.wheels = wheels;
    }

    @Override
    public Engine getEngine() {

        return engine;
    }

    @Override
    public Wheels getWheels() {

        return wheels;
    }
}
