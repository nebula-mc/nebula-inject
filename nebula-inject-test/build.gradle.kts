plugins {
    id("nebula-inject.library-conventions")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jspecify:jspecify:0.2.0")
    // TODO: Update to 0.3.0 when more tools add support

    compileOnly("org.junit.jupiter:junit-jupiter-api:5.9.3")
    api("org.mockito:mockito-core:5.4.0")
    api(project(":nebula-inject"))

    testImplementation("org.junit.platform:junit-platform-launcher:1.9.3")
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
        exclude("dev/nebulamc/inject/test/MockTest\$MultipleAnnotationsTest.class")
        exclude("dev/nebulamc/inject/test/MockTest\$InjectMockSubtypeTest.class")
        exclude("dev/nebulamc/inject/test/FactoryTest\$NonFactoryTest.class")
        exclude("dev/nebulamc/inject/test/FactoryTest\$MultipleAnnotationsTest.class")
        exclude("dev/nebulamc/inject/test/FactoryTest\$FactoryAndInjectTest.class")
        exclude("dev/nebulamc/inject/test/ServiceTest\$MultipleAnnotationsTest.class")

        useJUnitPlatform()
    }
}
