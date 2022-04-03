package org.dripto.pact

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*

@Component
class ConsoleInterface(val productService: ProductService): CommandLineRunner{
    lateinit var products: List<Product>

    override fun run(vararg args: String) {
        val scanner = Scanner(System.`in`)
        while (true) {
            printAllProducts()
            val choice = getUserChoice(scanner)
            if (choice == null || choice <= 0 || choice > products.size) {
                println("Exiting...")
                break
            }
            printProduct(choice)
        }
    }

    private fun printAllProducts() {
        println("\n\nProducts\n--------")
        productService.getAllProducts().forEachIndexed { index, product ->
            println("${index + 1} ${product.name}")
        }
    }

    private fun getUserChoice(scanner: Scanner): Int? {
        print("Select item to view details: ")
        return parseChoice(scanner.nextLine())
    }

    private fun printProduct(index: Int) {
        val id = products[index - 1].id
        try {
            println(productService.getProduct(id))
        } catch (e: Exception) {
            println("Failed to load product $id")
            println(e.message)
        }
    }

    private fun parseChoice(choice: String) = try { choice.toInt() } catch (e: NumberFormatException) { null }
}