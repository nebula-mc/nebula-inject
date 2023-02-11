rootProject.name = "nebula-inject"

dependencyResolutionManagement {
    includeBuild("build-logic")
}

sequenceOf(
    "nebula-inject",
    "nebula-inject-test"
).forEach { include(it) }
