/**
 * Nebula Inject.
 *
 * @author Sparky983
 * @since 0.2
 */
module dev.nebulamc.inject {
    requires static org.jspecify;

    exports dev.nebulamc.inject;
    exports dev.nebulamc.inject.util to dev.nebulamc.inject.test;
}
