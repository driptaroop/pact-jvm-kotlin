package org.dripto.pact

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class ProductService(val restTemplate: RestTemplate) {
    fun getAllProducts(): List<Product> = restTemplate.getForObject("/products")
    fun getProduct(id: String): Product = restTemplate.getForObject("/product/$id")
}