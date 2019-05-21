package ch.hellorin.challengames.service.provider

import ch.hellorin.challengames.configuration.ProviderConfiguration
import ch.hellorin.challengames.configuration.TestConfiguration
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataNeo4jTest
@ContextConfiguration(classes = [
    TestConfiguration::class,
    ProviderConfiguration::class
])
@ActiveProfiles("localtest")
@TestPropertySource(locations=["classpath:application.yaml"])
@Ignore
class GameProviderSystemTest {

    @Autowired
    lateinit var gameProvider: GameProvider

    @Test
    fun test() {
        gameProvider.tryToGetGame("Quake 1")
    }
}