# 🚀 Contract Testing in Kotlin (Pact JVM + Spring Boot)

Modern microservices don’t fail because of business logic.  
They fail because of API contract drift between independently deployed services.

This repository demonstrates a production-ready approach to contract testing in Kotlin microservices, ensuring API compatibility before deployment using Pact JVM and CI/CD gates.

---

## ⚠️ Problem: API Drift in Microservices

In monolithic systems, breaking a field is a compile-time error.

In microservices, it becomes a silent production issue:

- frontend expects `fullName`
- backend returns `name`
- CI passes
- production UI breaks without errors

This happens due to:

- independent deployments  
- missing API validation layer  
- outdated documentation (Swagger drift)  
- lack of machine-verifiable contracts  

---

## 🧠 Solution: Consumer-Driven Contract Testing

This project uses Pact-based contract testing in Kotlin to define API expectations at the consumer level and verify them independently in the provider pipeline.

---

## 🧪 Consumer Side (OrderService)

The consumer defines the API contract it depends on:

```kotlin
@Pact(consumer = "OrderService")
fun getUserPact(builder: PactDslWithProvider): V4Pact =
    builder
        .given("user with ID 42 exists")
        .uponReceiving("GET request for user 42")
        .path("/users/42")
        .method("GET")
        .willRespondWith()
        .status(200)
        .body(
            LambdaDsl.newJsonBody { obj ->
                obj.numberType("id", 42)
                obj.stringType("fullName", "Jane Doe")
            }.build()
        )
        .toPact(V4Pact::class.java)
