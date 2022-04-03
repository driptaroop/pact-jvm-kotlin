package org.dripto.pact.provider

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RestController
class ProductController(val productRepository: ProductRepository){
    @GetMapping("products")
    fun getAllProducts(): List<Product> = productRepository.fetchAll()

    @GetMapping("product/{id}")
    fun getProductById(@PathVariable id: String): ResponseEntity<Product> {
        val product = productRepository.getById(id)
        return ResponseEntity.of(Optional.ofNullable(product))
    }
}