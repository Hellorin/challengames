package ch.hellorin.challengames.configuration

import org.apache.http.impl.client.HttpClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory

@Configuration
class HttpConfiguration {
    @Value("\${igdb.api-key}")
    val apiKey: String? = null

    @Bean("igdb-http")
    fun igdbHttpHeaders() : HttpHeaders {
        var httpHeaders = HttpHeaders()
        httpHeaders.set("user-key", apiKey)
        httpHeaders.contentType = MediaType.TEXT_PLAIN
        httpHeaders.accept = listOf(MediaType.APPLICATION_JSON)

        return httpHeaders
    }

    @Bean
    fun httpRequestFactory() : HttpComponentsClientHttpRequestFactory {
        val httpClient = HttpClients.custom()
     //           .setSSLHostnameVerifier(NoopHostnameVerifier())
                .build()

        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.httpClient = httpClient

        return requestFactory
    }

}