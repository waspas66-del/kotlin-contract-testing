package contract

import au.com.dius.pact.consumer.dsl.LambdaDsl
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(providerName = "UserService", port = "8080")
class OrderConsumerTest {

    @Pact(consumer = "OrderService")
    fun getUserPact(builder: PactDslWithProvider): V4Pact {
        return builder
            .given("user with ID 42 exists")
            .uponReceiving("GET request for user 42")
            .path("/users/42")
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(
                LambdaDsl.newJsonBody { obj ->
                    obj.numberType("id", 42)
                    obj.stringType("email", "jane@example.com")
                    obj.stringType("fullName", "Jane Doe")
                }.build()
            )
            .toPact(V4Pact::class.java)
    }

    @Test
    @PactTestFor(pactMethod = "getUserPact")
    fun `should verify user contract`() {
        given()
            .port(8080)
            .get("/users/42")
        .then()
            .statusCode(200)
            .body("email", equalTo("jane@example.com"))
    }
}
