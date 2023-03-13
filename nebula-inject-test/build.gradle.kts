plugins {
    id("nebula-inject.library-conventions")
}

sourceSets {
    create("intTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val intTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
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

    intTestImplementation("org.junit.platform:junit-platform-launcher:1.9.2")
    intTestImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    intTestImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withJavadocJar()
    withSourcesJar()
}

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["intTest"].output.classesDirs
    classpath = sourceSets["intTest"].runtimeClasspath
    shouldRunAfter("test")

    // Used for integration testing with JUnit
    exclude("dev/nebulamc/inject/test/MockTest\$InjectAndMockFieldTest.class")
    exclude("dev/nebulamc/inject/test/FactoryTest\$NonFactoryTest.class")
    exclude("dev/nebulamc/inject/test/FactoryTest\$FactoryAndMockTest.class")
    exclude("dev/nebulamc/inject/test/FactoryTest\$FactoryAndInjectTest.class")

    useJUnitPlatform()

    testLogging {
        events("passed")
    }
}

tasks {
    check {
        dependsOn(integrationTest)
    }
    test {
        useJUnitPlatform()
    }
}
