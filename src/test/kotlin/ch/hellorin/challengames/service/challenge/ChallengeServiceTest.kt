package ch.hellorin.challengames.service.challenge

import ch.hellorin.challengames.configuration.ProviderConfiguration
import ch.hellorin.challengames.configuration.TestConfiguration
import ch.hellorin.challengames.exception.ExistingChallengeException
import ch.hellorin.challengames.exception.MissingChallengeException
import ch.hellorin.challengames.service.IUserService
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
import java.security.Principal
import java.util.*


@ContextConfiguration(classes = [
    TestConfiguration::class
])
@DataNeo4jTest(excludeAutoConfiguration = [ProviderConfiguration::class])
@ActiveProfiles("localtest")
@RunWith(SpringRunner::class)
class ChallengeServiceTest {

    @Autowired
    lateinit var challengeService: IChallengeAdminService

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var gameProvider: IGameProvider

    lateinit var principal: Principal

    @Test
    fun `add a new challenge that doesn't exist yet with connected user`() {
        // Given
        val deadline = Date()

        principal = Principal { "user" }

        userService.addUser("user", "a@mail.com", "pwd", listOf("User"))

        Mockito.`when`(gameProvider.tryToGetGame("Doom")).thenReturn(GameDataDto(1, "Doom", "Doom", "Doom"))

        // When
        val challengeId = challengeService.newChallenge(
                "Challenge4000",
                "description",
                deadline,
                "Doom",
                null,
                "user",
                principal)

        // Then
        val challengeById = challengeService.getChallengeById(challengeId)

        Assert.assertNotNull(challengeId)
        Assert.assertNotNull(challengeById.id)
        Assert.assertEquals("Challenge4000", challengeById.name)
        Assert.assertEquals("description", challengeById.description)
        Assert.assertEquals(deadline, challengeById.deadline)
        Assert.assertNotNull(challengeById.game)
        Assert.assertEquals("Doom", challengeById.game.name)
        Assert.assertNotNull(challengeById.challengee)
        Assert.assertEquals("user", challengeById.challengee.name)
    }

    @Test(expected = ExistingChallengeException::class)
    fun `add a new challenge that already exist yet with connected user`() {
        // Given
        val deadline = Date()

        principal = Principal { "user" }

        userService.addUser("user", "a@mail.com", "pwd", listOf("User"))

        Mockito.`when`(gameProvider.tryToGetGame("Doom")).thenReturn(GameDataDto(1, "Doom", "Doom", "Doom"))

        challengeService.newChallenge(
                "Challenge4000",
                "description",
                deadline,
                "Doom",
                null,
                "user",
                principal)

        // When
        challengeService.newChallenge(
                "Challenge4000",
                "description",
                deadline,
                "Doom",
                null,
                "user",
                principal)

        // Then
    }

    @Test
    fun `add a new challenge that doesn't exist yet with username`() {
        // Given
        val deadline = Date()

        userService.addUser("user", "a@mail.com", "pwd", listOf("User"))

        Mockito.`when`(gameProvider.tryToGetGame("Doom")).thenReturn(GameDataDto(1, "Doom", "Doom", "Doom"))

        // When

        val challengeId = challengeService.newChallenge(
                "Challenge4000",
                "description",
                deadline,
                "Doom",
                null,
                "user",
                "user")

        // Then
        val challengeById = challengeService.getChallengeById(challengeId)

        Assert.assertNotNull(challengeId)
        Assert.assertNotNull(challengeById.id)
        Assert.assertEquals("Challenge4000", challengeById.name)
        Assert.assertEquals("description", challengeById.description)
        Assert.assertEquals(deadline, challengeById.deadline)
        Assert.assertNotNull(challengeById.game)
        Assert.assertEquals("Doom", challengeById.game.name)
        Assert.assertNotNull(challengeById.challengee)
        Assert.assertEquals("user", challengeById.challengee.name)
    }

    @Test(expected = ExistingChallengeException::class)
    fun `add a new challenge that already exist yet with username`() {
        // Given
        val deadline = Date()

        userService.addUser("user", "a@mail.com", "pwd", listOf("User"))

        Mockito.`when`(gameProvider.tryToGetGame("Doom")).thenReturn(GameDataDto(1, "Doom", "Doom", "Doom"))

        challengeService.newChallenge(
                "Challenge4000",
                "description",
                deadline,
                "Doom",
                null,
                "user",
                "user")

        // When
        challengeService.newChallenge(
                "Challenge4000",
                "description",
                deadline,
                "Doom",
                null,
                "user",
                "user")

        // Then
    }

    @Test(expected = MissingChallengeException::class)
    fun `get a challenge that doesn't exist`() {
        // Given

        // When
        challengeService.getChallenge("Challenge4000")

        // Then
    }

    @Test
    fun `get a challenge that exists`() {
        // Given
        principal = Principal { "user" }

        userService.addUser("user", "a@mail.com", "pwd", listOf("User"))

        Mockito.`when`(gameProvider.tryToGetGame("Doom")).thenReturn(GameDataDto(1, "Doom", "Doom", "Doom"))

        val challengeId = challengeService.newChallenge(
                "Challenge4000",
                "description",
                Date(),
                "Doom",
                null,
                "user",
                principal)

        // When
        val challengeById = challengeService.getChallenge("Challenge4000")

        // Then
        Assert.assertEquals(challengeId, challengeById.id)
    }
}