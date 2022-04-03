package org.dripto.pact.consumer

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ProductServiceConfig {
    @Bean
    fun productRestTemplate(@Value("\${provider.port:8085}") port: Int): RestTemplate =
        RestTemplateBuilder().rootUri("http://localhost:$port").build()
}