package org.dripto.pact.consumer

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonArrayMinLike
import au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody
import au.com.dius.pact.consumer.dsl.PactBuilder
import au.com.dius.pact.consumer.dsl.newJsonArray
import au.com.dius.pact.consumer.dsl.newJsonObject
import au.com.dius.pact.consumer.dsl.newObject
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.web.client.RestTemplateBuilder

@ExtendWith(PactConsumerTestExt::class)
class ProductConsumerPact {
    @Pact(consumer = "FrontendApplication", provider = "ProductService")
    fun getAllProducts(builder: PactBuilder): V4Pact {
        return builder.usingLegacyDsl().given("products exist")
            .uponReceiving("get all products")
            .method("GET")
            .path("/products")
            .willRespondWith()
            .status(200)
            .headers(headers())
            .body(
                newJsonArrayMinLike(2) {
                    it.newObject {
                        stringType("id", "09")
                        stringType("type", "CREDIT_CARD")
                        stringType("name", "Gem Visa")
                    }
            }.build())
            .toPact(V4Pact::class.java)
    }

    @Pact(consumer = "FrontendApplication", provider = "ProductService")
    fun getOneProduct(builder: PactBuilder): V4Pact {
        return builder.usingLegacyDsl().given("product with ID 10 exists")
            .uponReceiving("get product with ID 10")
            .method("GET")
            .path("/products/10")
            .willRespondWith()
            .status(200)
            .headers(headers())
            .body(newJsonObject {
                stringType("id", "10")
                stringType("type", "CREDIT_CARD")
                stringType("name", "28 Degrees")
            })
            .toPact(V4Pact::class.java)
    }

    @Test
    @PactTestFor(pactMethod = "getAllProducts")
    fun getAllProducts_whenProductsExist(mockServer: MockServer) {
        val product = Product("09", "CREDIT_CARD", "Gem Visa")
        val expected = listOf(product, product)
        val restTemplate = RestTemplateBuilder()
            .rootUri(mockServer.getUrl())
            .build()
        val products = ProductService(restTemplate).getAllProducts()
        assertEquals(expected, products)
    }

    @Test
    @PactTestFor(pactMethod = "getOneProduct")
    fun getProductById_whenProductWithId10Exists(mockServer: MockServer) {
        val expected = Product("10", "CREDIT_CARD", "28 Degrees")
        val restTemplate = RestTemplateBuilder()
            .rootUri(mockServer.getUrl())
            .build()
        val product = ProductService(restTemplate).getProduct("10")
        assertEquals(expected, product)
    }

    private fun headers(): Map<String, String> = mapOf("Content-Type" to "application/json; charset=utf-8")
}