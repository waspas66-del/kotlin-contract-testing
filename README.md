# kotlin-contract-testing
Insights on why APIs break in production and how Kotlin contract testing prevents interface drift. Includes consumer- and provider-side patterns, CI/CD integration, and lessons from real-world microservices, helping engineers ensure reliable, verifiable, and independent deployments.
# Kotlin Contract Testing: Kill API Drift in Production 🚀

This repository demonstrates how to eliminate production failures caused by breaking API changes using **Consumer-Driven Contracts**.

## 🎯 The Core Problem
In a monolith, renaming a field is a compiler error. In microservices, it’s a runtime bomb. Integration tests are often too slow, flaky, or run against incompatible versions. Contract testing makes interface failures structurally impossible.

## 🛠 Tech Stack
- **Language:** Kotlin + DSL
- **Testing:** JUnit 5, MockK, Rest-Assured
- **Framework:** Pact JVM (4.6.5)
- **Deployment Safety:** Pact Broker + `can-i-deploy`

---

## 🏗 How It Works

### 1. Consumer Side (OrderService)
The consumer defines exactly what it needs. Using `stringType` and `numberType` ensures tests focus on **structure**, not volatile data.

```kotlin
// Define the contract
LambdaDsl.newJsonBody { obj ->
    obj.numberType("id", 42)
    obj.stringType("fullName", "Jane Doe")
}.build()
