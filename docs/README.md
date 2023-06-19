# Getting Started

## About

Nebula Inject is a minimal dependency injection library for Java.

## Installation

Add the following to your build file:

{% tabs %}
{% tab title="Gradle (Kotlin DSL)" %}
```kts
repositories {
    maven("https://repo.nebulamc.dev/releases")
}

dependencies {
    implementation("dev.nebulamc:nebula-inject:0.1")
}
```
{% endtab %}

{% tab title="Gradle (Groovy DSL)" %}
```groovy
repositories {
    maven {
        url = 'https://repo.nebulamc.dev/releases'
    }
}

dependencies {
    implementation 'dev.nebulamc:nebula-inject:0.1'
}
```
{% endtab %}
{% endtabs %}
