/**
 * Contains the Nebula Inject classes.
 *
 * <h2><a id="injectable-constructors">Injectable Constructors</a></h2>
 * Injectable constructors define where Nebula Inject should perform
 * <a href="https://en.wikipedia.org/wiki/Dependency_injection#Constructor_injection">
*     constructor injection
* </a> on a client object. By default, a single, no-args constructor is injectable, or any
 * constructors annotated with {@link dev.nebulamc.inject.Inject} (called "inject constructors") are
 * also injectable.
 *
 * @see dev.nebulamc.inject.Container
 * @since 0.1
 */
package dev.nebulamc.inject;
