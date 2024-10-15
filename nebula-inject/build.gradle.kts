plugins {
    id("nebula-inject.library-conventions")
}

dependencies {
    compileOnly("org.jspecify:jspecify:1.0.0")

    testImplementation("org.mockito:mockito-core:5.14.2")
}
