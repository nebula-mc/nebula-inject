rootProject.name = "nebula-inject"

sequenceOf(
    "nebula-inject",
    "nebula-inject-test"
).forEach { include(it) }
