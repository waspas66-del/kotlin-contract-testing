🚀 Contract Testing in Kotlin (Pact JVM + Spring Boot)

Modern microservices don’t fail because of business logic.
They fail because of API contract drift between independently deployed services.

This repository demonstrates a production-ready approach to contract testing in Kotlin microservices, ensuring API compatibility before deployment using Pact JVM and CI/CD gates.

⚠️ Problem: API Drift in Microservices

In monolithic systems, breaking a field is a compile-time error.

In microservices, it becomes a silent production issue:

frontend expects fullName
backend returns name
CI passes
production UI breaks without errors

This happens due to:

independent deployments
missing API validation layer
outdated documentation (Swagger drift)
lack of machine-verifiable contracts
🧠 Solution: Consumer-Driven Contract Testing

This project uses Pact-based contract testing in Kotlin to define API expectations at the consumer level and verify them independently in the provider pipeline.

🧪 Consumer Side (OrderService)

The consumer defines the API contract it depends on:

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

👉 This generates a versioned contract artifact (Pact file) that defines expected API structure.

🏗 Provider Verification (UserService)

The provider independently verifies that its implementation matches the contract:

@TestTemplate
@ExtendWith(PactVerificationInvocationContextProvider::class)
fun verifyPact(context: PactVerificationContext) {
    context.verifyInteraction()
}

@State("user with ID 42 exists")
fun `seed user`() {
    every { userRepository.findById(42L) } returns Optional.of(
        User(
            id = 42L,
            email = "jane@example.com",
            fullName = "Jane Doe"
        )
    )
}

👉 If the provider changes a field (e.g. fullName → name), the CI pipeline fails before deployment.

🔄 CI/CD Integration (Deployment Safety Layer)

Contract validation is enforced through CI/CD gates:

pact-broker can-i-deploy \
  --pacticipant UserService \
  --version ${GIT_COMMIT} \
  --to-environment staging

👉 This ensures:

no breaking API changes reach production
deployments are blocked on contract violations
compatibility is verified across services before release
🧩 Architecture Overview
Consumer (OrderService)
defines expectations
generates Pact contract
runs fast unit-style tests
Provider (UserService)
loads contract
verifies implementation
fails CI if contract is broken
Pact Broker
stores versioned contracts
coordinates multi-service verification
📌 What This Solves

This approach eliminates:

silent API breaking changes
frontend/backend mismatch issues
reliance on shared staging environments
manual API coordination between teams

Instead, API compatibility becomes machine-verifiable in CI/CD pipelines.

🧪 Tech Stack
Kotlin DSL
Spring Boot
JUnit 5
MockK
Pact JVM 4.6+
Pact Broker / PactFlow
GitHub Actions / CI pipelines
🚀 When to Use Contract Testing

Best suited for systems where:

services deploy independently
multiple teams consume the same API
frontend and backend evolve separately
integration tests are too slow or unreliable

Not necessary for:

monolithic applications
tightly coupled single-team systems
🔗 Learn More

Full breakdown, real-world patterns, and deeper explanation:

👉 https://krun.pro/kotlin-testing/

Author: Krun Dev — software engineer and systems analyst focused on backend architecture, distributed systems, and API reliability in modern microservice environments.

He works on analyzing production failures, improving service-to-service communication patterns, and researching practical approaches to testing and deployment safety in cloud-native systems.
