plugins {
    id("nebula-inject.library-conventions")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jspecify:jspecify:0.2.0")
    // TODO: Update to 0.3.0 when more tools add support

    compileOnly("org.junit.jupiter:junit-jupiter-api:5.9.0")
    api("org.mockito:mockito-core:4.11.0")
    api(project(":nebula-inject"))

    testImplementation("org.junit.platform:junit-platform-launcher:1.9.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withJavadocJar()
    withSourcesJar()
}

tasks {
    test {
        // Used for integration testing with JUnit
        exclude("dev/nebulamc/inject/test/NebulaInjectTestTest\$InjectAndMockFieldTest.class")

        useJUnitPlatform()
    }
}
