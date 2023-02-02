package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;

@NullMarked
final class InjectServiceDefinitionFactoryImpl implements InjectServiceDefinitionFactory {

    @Override
    public <T> ServiceDefinition<T> createServiceDefinition(
            final Class<T> type,
            final Class<? extends T> implementation) {

        Preconditions.requireNonNull(type, "implementation");
        Preconditions.requireNonNull(implementation, "implementation");

        return new InjectServiceDefinition<>(type, implementation);
    }
}
