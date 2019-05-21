package ch.hellorin.challengames.configuration

import ch.hellorin.challengames.service.provider.GameProvider
import ch.hellorin.challengames.service.provider.GameSelectionStrategy
import ch.hellorin.challengames.service.provider.IGameProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory

@Configuration
class ProviderConfiguration {
    @Bean
    fun gameProvider(httpRequestFactory: HttpComponentsClientHttpRequestFactory,
                     @Qualifier("igdb-http")
                     httpHeaders: HttpHeaders,
                     @Qualifier("oneResult")
                     gameSelectionStrategy: GameSelectionStrategy) : IGameProvider = GameProvider(httpRequestFactory, httpHeaders, gameSelectionStrategy)
}