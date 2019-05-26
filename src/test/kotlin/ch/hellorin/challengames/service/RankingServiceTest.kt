package ch.hellorin.challengames.service

import ch.hellorin.challengames.configuration.ProviderConfiguration
import ch.hellorin.challengames.configuration.TestConfiguration
import ch.hellorin.challengames.service.challenge.IChallengeAdminService
import ch.hellorin.challengames.service.challenge.IChallengeService
import ch.hellorin.challengames.service.provider.GameDataDto
import ch.hellorin.challengames.service.provider.IGameProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@ContextConfiguration(classes = [
    TestConfiguration::class
])
@DataNeo4jTest(excludeAutoConfiguration = [ProviderConfiguration::class])
@ActiveProfiles("localtest")
@Transactional
@RunWith(SpringRunner::class)
class RankingServiceTest {
    @Autowired
    lateinit var rankingService: IRankingService

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var gameProvider: IGameProvider

    @Autowired
    lateinit var challengeService: IChallengeAdminService

    @Before
    fun setup() {
        userService.addUser("user1", "a@mail.com", "pwd", listOf("User"))
        userService.addUser("user2", "a@mail.com", "pwd", listOf("User"))

        Mockito.`when`(gameProvider.tryToGetGame("Doom")).thenReturn(GameDataDto(1, "Doom", "Doom", "Doom"))
    }

    @Test
    fun `get ranking`() {
        // Given
        challengeService.newChallenge(challengeName = "Challenge1", description = "desc", gameName = "Doom", gameId= null, challengeeName="user1", username = "user2")
        challengeService.newChallenge(challengeName = "Challenge2", description = "desc", gameName = "Doom", gameId= null, challengeeName="user2", username = "user1")

        val ranking = rankingService.getRanking()
        Assert.assertEquals(2, ranking.nbChallenges)
    }

}