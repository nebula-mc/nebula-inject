package dev.nebulamc.inject.car;

import dev.nebulamc.inject.Factory;
import dev.nebulamc.inject.Service;

@Factory
public final class CarFactory {

    @Service
    private Car createCar(final Engine engine, final Wheels wheels) {

        return new Suv(engine, wheels);
    }
}
