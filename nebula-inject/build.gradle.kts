plugins {
    id("nebula-inject.library-conventions")
}

dependencies {
    compileOnly("org.jspecify:jspecify:1.0.0")
    // TODO: Update to 0.3.0 when more tools add support

    testImplementation("org.mockito:mockito-core:5.13.0")
}
