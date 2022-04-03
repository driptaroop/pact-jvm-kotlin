package org.dripto.pact

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.web.client.RestTemplateBuilder


internal class ProductServiceTest {
    lateinit var wireMockServer: WireMockServer
    lateinit var productService: ProductService

    @BeforeEach
    fun setUp() {
        wireMockServer = WireMockServer(options().dynamicPort())
        wireMockServer.start()
        val restTemplate = RestTemplateBuilder()
            .rootUri(wireMockServer.baseUrl())
            .build()
        productService = ProductService(restTemplate)
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.stop()
    }

    @Test
    fun getAllProducts() {
        wireMockServer.stubFor(
            get(urlPathEqualTo("/products"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """
                                [
                                  {"id":"9","type":"CREDIT_CARD","name":"GEM Visa","version":"v2"},
                                  {"id":"10","type":"CREDIT_CARD","name":"28 Degrees","version":"v1"}
                                 ]"
                            """.trimIndent()
                        )
                )
        )
        val expected: List<Product> = listOf(
            Product("9", "CREDIT_CARD", "GEM Visa", "v2"),
            Product("10", "CREDIT_CARD", "28 Degrees", "v1")
        )
        val products = productService.getAllProducts()
        assertEquals(expected, products)
    }

    @Test
    fun getProductById() {
        wireMockServer.stubFor(
            get(urlPathEqualTo("/products/50"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {"id":"50","type":"CREDIT_CARD","name":"28 Degrees","version":"v1"}
                            """.trimIndent())
                )
        )
        val expected = Product("50", "CREDIT_CARD", "28 Degrees", "v1")
        val product = productService.getProduct("50")
        assertEquals(expected, product)
    }
}