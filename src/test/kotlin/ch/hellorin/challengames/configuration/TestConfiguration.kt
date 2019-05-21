package ch.hellorin.challengames.configuration

import ch.hellorin.challengames.service.provider.IGameProvider

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

@TestConfiguration
@EntityScan("ch.hellorin.challengames.persistance.model")
@Import(value = [
    CachingConfiguration::class,
    HttpConfiguration::class,
    SecurityConfiguration::class,
    RolesConfiguration::class
])
@Profile("localtest")
class TestConfiguration {
    @MockBean(name= "gameProvider")
    lateinit var gameProvider: IGameProvider
}