import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    `java-library`
    jacoco
    id("com.vanniktech.maven.publish")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.3")
}

val isSnapshot = project.version.toString().endsWith("-SNAPSHOT")

mavenPublishing {
    configure(JavaLibrary(
        javadocJar = JavadocJar.Javadoc(),
        sourcesJar = true,
    ))
    signAllPublications()
    if (!isSnapshot) {
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    }

    pom {
        description.set(project.description ?: throw IllegalStateException("Add a project description"))
        url.set("https://github.com/nebula-mc/nebula-inject")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://www.opensource.org/licenses/mit-license")
            }
        }
        developers {
            developer {
                id.set("Sparky983")
            }
        }
        scm {
            url.set("https://github.com/nebula-mc/nebula-inject/")
            connection.set("scm:git:git://github.com/nebula-mc/nebula-inject.git")
            developerConnection.set("scm:git:ssh://git@github.com/nebula-mc/nebula-inject.git")
        }
    }
}

publishing {
    repositories {
        if (isSnapshot) {
            maven {
                name = "nebula"
                url = uri("https://repo.nebulamc.dev/snapshots")
                credentials(PasswordCredentials::class)
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
    jacocoTestReport {
        reports {
            xml.required.set(true)
        }
    }
}
