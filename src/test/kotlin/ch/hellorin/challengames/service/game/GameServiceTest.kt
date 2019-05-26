package ch.hellorin.challengames.service.game

import ch.hellorin.challengames.configuration.TestConfiguration
import ch.hellorin.challengames.service.game.IGameService
import ch.hellorin.challengames.service.provider.GameDataDto
import ch.hellorin.challengames.service.provider.IGameProvider
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataNeo4jTest
@ContextConfiguration(classes = [
    TestConfiguration::class
])
@ActiveProfiles("localtest")
class GameServiceTest {

    @Autowired
    lateinit var gameService: IGameService

    @Autowired
    lateinit var gameProvider: IGameProvider

    @Test
    fun `add a game that doesn't exist`() {
        // Given
        Mockito.`when`(gameProvider.tryToGetGame("Doom")).thenReturn(GameDataDto(1, "Doom", "Doom", "Doom"))

        // When
        gameService.addGame("Doom")

        // Then
        var game = gameService.getGame("Doom")
        Assert.assertNotNull(game)
        Assert.assertEquals("Doom", game.name)
    }

}

