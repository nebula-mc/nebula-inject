/**
 * Nebula Inject Test.
 *
 * @author Sparky983
 * @since 0.2
 */
module dev.nebulamc.inject.test {
    requires static org.jspecify;
    requires static org.junit.jupiter.api;

    requires transitive dev.nebulamc.inject;
    requires transitive org.mockito;

    exports dev.nebulamc.inject.test;
}
