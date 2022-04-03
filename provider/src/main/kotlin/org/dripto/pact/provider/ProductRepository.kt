package org.dripto.pact.provider

import org.springframework.stereotype.Repository

@Repository
class ProductRepository {
    private val products: MutableMap<String, Product> = mutableMapOf()

    init { initProducts() }
    fun fetchAll(): List<Product> = products.values.toList()
    fun getById(id: String): Product? = products[id]

    private fun initProducts() {
        products["09"] = Product("09", "CREDIT_CARD", "Gem Visa", "v1")
        products["10"] = Product("10", "CREDIT_CARD", "28 Degrees", "v1")
        products["11"] = Product("11", "PERSONAL_LOAN", "MyFlexiPay", "v2")
    }
}