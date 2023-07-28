# Dependency Injection

Dependency injection is a software design pattern that allows us to create testable and reusable modules.

## What Is Dependency Injection?

Traditionally, classes would instantiate their dependencies:

{% tabs %}
{% tab title="Java" %}
```java
class Car {
    Engine engine = new PetrolEngine();
}

Car car = new Car();
```
{% endtab %}

{% tab title="Kotlin" %}
```kotlin
class Car {
    val engine = PetrolEngine()   
}

val car = Car()
car.start()
```
{% endtab %}
{% endtabs %}

This has many issues such as:

* Couples the `Car` (the client) to the `PetrolEngine` (the dependency)&#x20;
* Difficult to test
* Violates the [Single-responsibility Principle](https://en.wikipedia.org/wiki/Single-responsibility\_principle) as the `Car` is responsible for engine construction

Dependency injection aims to resolve these issues by inverting the responsibility of client object construction to an external entity, usually referred to as the "injector" or "container".&#x20;

## Constructor Injection

There are many forms of dependency injection, but the simplest is known as constructor injection. Instead of the `Car` creating an engine, we can take it in inside the constructor:

{% tabs %}
{% tab title="Java" %}
```java
class Car {
    Engine engine;

    Car(Engine engine) {
        this.engine = engine;
    }
}
```
{% endtab %}

{% tab title="Kotlin" %}
```kotlin
class Car(val engine: Engine)
```
{% endtab %}
{% endtabs %}

However, this complicates construction.

{% tabs %}
{% tab title="Java" %}
```java
Engine engine = new PetrolEngine();
Car car = new Car(engine);
```
{% endtab %}

{% tab title="Kotlin" %}
```kotlin
val engine = PetrolEngine()
val car = Car(engine)
car.start()
```
{% endtab %}
{% endtabs %}

This sort of dependency injection is known as "manual dependency injection" or "manual wiring".

## Nebula Inject

Nebula Inject eliminates the need for manual dependency injection by wiring together and managing dependencies for you.

{% hint style="info" %}
Wiring: The process of finding and injecting dependencies.
{% endhint %}

### Injection

You can use `@Inject` to tell Nebula Inject to where to inject our dependencies.&#x20;

{% tabs %}
{% tab title="Java" %}
```java
class Car {
    Engine engine;
    
    @Inject
    Car(Engine engine) {
        this.engine = engine;
    }
}
```
{% endtab %}

{% tab title="Kotlin" %}
```kotlin
class Car @Inject constructor(private val engine: Engine)
```
{% endtab %}
{% endtabs %}

In this case, whenever we want to create a `Car`, Nebula Inject will find the `Engine` dependency and instantiate the `Car` using that engine.&#x20;

#### Injecting Collection Types

Nebula Inject can also inject collection types such as `List` and `Set`:

{% tabs %}
{% tab title="Java" %}
```java
class Car {
    List<Wheel> wheels;
    
    @Inject
    Car(List<Wheel> wheels) {
        this.wheels = wheels;
    }
}
```
{% endtab %}

{% tab title="Kotlin" %}
```kotlin
class Car @Inject constructor(private val wheels: List<Wheel>)
```
{% endtab %}
{% endtabs %}

The following collection types are supported:

* `java.lang.Iterable`
* `java.util.Collection`
* `java.util.List`
* `java.util.Set`
* Arrays

### Dependency Resolution

Currently, Nebula Inject cannot find the `Engine` dependency because it doesn't know what implementation to use. By default, Nebula Inject cannot resolve interfaces as it can only resolve concrete classes with either an `@Inject` constructor or a no-args constructor. &#x20;

### Factories

Factories allow us to provide dependencies for any type including interfaces and external types. To define a factory we can use the `@Factory` annotation followed by a provider method with the `@Service` annotation:

{% hint style="info" %}
Service: Any object that can be injected. In this case both the `Car` and `Engine` are services.
{% endhint %}

{% tabs %}
{% tab title="Java" %}
```java
@Factory
class EngineFactory {
    @Service
    Engine provideEngine() {
        return new PetrolEngine();
    }
}
```
{% endtab %}

{% tab title="Kotlin" %}
```kotlin
@Factory
class EngineFactory {
    @Service
    fun provideEngine(): Engine {
        return PetrolEngine()
    }
}
```
{% endtab %}
{% endtabs %}

Alternatively, we can ask Nebula Inject to provide a `PetrolEngine` to us since it has a default no-args constructor.

{% tabs %}
{% tab title="Java" %}
<pre class="language-java"><code class="lang-java"><strong>@Factory
</strong><strong>class EngineFactory {
</strong><strong>    @Service
</strong>    Engine provideEngine(PetrolEngine engine) {
        return engine;
    }
}
</code></pre>
{% endtab %}

{% tab title="Kotlin" %}
```kotlin
@Factory
class EngineFactory {
    @Service
    fun provideEngine(engine: PetrolEngine): Engine {
        return engine
    }
}
```
{% endtab %}
{% endtabs %}

### Building the Container

Building the container is fairly straightforward, we just need to specify our factories:

{% tabs %}
{% tab title="Java" %}
```java
Container container = Container.builder()
        .factory(new EngineFactory())
        .build();

Car car = container.findService(Car.class);
```
{% endtab %}

{% tab title="Kotlin" %}
```kotlin
val container = Container.builder()
        .factory(EngineFactory())
        .build()

val car = container.findService(Car::class.java)
```
{% endtab %}
{% endtabs %}

Additionally, we can manually add objects to the container:

{% tabs %}
{% tab title="Java" %}
```java
Container container = Container.builder()
        .singleton(new PetrolEngine())
        .build();
```
{% endtab %}

{% tab title="Kotlin" %}
```kotlin
val container = Container.builder()
        .singleton(PetrolEngine())
        .build()
```
{% endtab %}
{% endtabs %}

However, this is only recommended for external objects as you should use [factories](dependency-injection.md#factories) instead.
