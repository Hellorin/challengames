package ch.hellorin.challengames.service.rating

import ch.hellorin.challengames.configuration.ProviderConfiguration
import ch.hellorin.challengames.configuration.TestConfiguration
import ch.hellorin.challengames.exception.MissingChallengeException
import ch.hellorin.challengames.exception.MissingUserException
import ch.hellorin.challengames.exception.NotARaterException
import ch.hellorin.challengames.persistance.model.node.Game
import ch.hellorin.challengames.persistance.model.node.Role
import ch.hellorin.challengames.persistance.repository.ChallengeRepository
import ch.hellorin.challengames.persistance.repository.GameRepository
import ch.hellorin.challengames.persistance.repository.RoleRepository
import ch.hellorin.challengames.service.user.UserService
import ch.hellorin.challengames.service.challenge.ChallengeService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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
class RatingServiceTest {
    @Autowired
    lateinit var target: IRatingService

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var challengeService: ChallengeService

    @Autowired
    lateinit var challengeRepository: ChallengeRepository

    @Autowired
    lateinit var gameRepository: GameRepository

    @Before
    fun setup() {
        roleRepository.save(Role("Role"))

        userService.addUser("user", "email", "pwd", listOf("User"))

        gameRepository.save(Game("doom"))
    }

    @Test(expected = MissingChallengeException::class)
    fun `try to become a rater on a missing challenge (with challengeId)`() {
        // Given
        val principal = Principal { "user" }

        // When
        target.becomeARater(1, principal)
    }

    @Test(expected = MissingUserException::class)
    fun `try to become a rater with an unknown user (shouldn't happen ever) (with challengeId)`() {
        // Given
        val principal = Principal { "user2" }

        var newChallengeId = challengeService.newChallenge("challenge1", "desc", Date(), "doom", null, "user", "user1")

        // When
        target.becomeARater(newChallengeId, principal)
    }

    @Test
    fun `become a rater (with challengeId)`() {
        // Given
        val principal = Principal { "user" }

        var newChallengeId = challengeService.newChallenge("challenge1", "desc", Date(), "doom", null, "user", "user")

        // When
        target.becomeARater(newChallengeId, principal)

        //Then
        var challenge = challengeRepository.findById(newChallengeId).get()

        Assert.assertEquals(1, challenge.raters.size)
        Assert.assertEquals("user", challenge.raters.iterator().next().name)
    }

    @Test(expected = MissingChallengeException::class)
    fun `try to become a rater on a missing challenge (with challengeName)`() {
        // Given
        val principal = Principal { "user" }

        // When
        target.becomeARater("challengeThatDoesntExist", principal)
    }

    @Test(expected = MissingUserException::class)
    fun `try to become a rater with an unknown user (shouldn't happen ever) (with challengeName)`() {
        // Given
        val principal = Principal { "user2" }

        var newChallengeId = challengeService.newChallenge("challenge1", "desc", Date(), "doom", null, "user", "user1")

        // When
        target.becomeARater("challenge1", principal)
    }

    @Test
    fun `become a rater (with challengeName)`() {
        // Given
        val principal = Principal { "user" }

        var newChallengeId = challengeService.newChallenge("challenge1", "desc", Date(), "doom", null, "user", "user")

        // When
        target.becomeARater("challenge1", principal)

        //Then
        var challenge = challengeRepository.findById(newChallengeId).get()

        Assert.assertEquals(1, challenge.raters.size)
        Assert.assertEquals("user", challenge.raters.iterator().next().name)
    }

    @Test(expected = MissingChallengeException::class)
    fun `rate an unknown challenge (with challengeId)`() {
        // Given
        val principal = Principal { "user3" }

        // When
        target.rateChallenge(560, principal, 2)
    }

    @Test(expected = MissingUserException::class)
    fun `rate a challenge with an unknown connected user (shouldn't never happen) (with challengeId)`() {
        // Given
        val principal = Principal { "user3" }

        var newChallengeId = challengeService.newChallenge("challenge1", "desc", Date(), "doom", null, "user", "user")

        // When
        target.rateChallenge(newChallengeId, principal, 2)
    }

    @Test(expected = NotARaterException::class)
    fun `rate a challenge in which we are not rater (with challengeId)`() {
        // Given
        val principal = Principal { "user" }

        var newChallengeId = challengeService.newChallenge("challenge1", "desc", Date(), "doom", null, "user", "user")

        // When
        target.rateChallenge(newChallengeId, principal, 2)
    }

    @Test
    fun `rate a challenge in which we are a rater (with challengeId)`() {
        // Given
        val principal = Principal { "user" }

        var newChallengeId = challengeService.newChallenge("challenge1", "desc", Date(), "doom", null, "user", "user")

        target.becomeARater("challenge1", principal)

        // When
        target.rateChallenge(newChallengeId, principal, 2)

        // Then
        var challenge = challengeRepository.findById(newChallengeId).get()

        Assert.assertEquals(1, challenge.raters.size)
        Assert.assertEquals("user", challenge.raters.iterator().next().name)
        Assert.assertEquals(1, challenge.ratings.size)
        val rating = challenge.ratings.iterator().next()
        Assert.assertEquals("user", rating.user.name)
        Assert.assertEquals("challenge1", rating.challenge.name)
        Assert.assertEquals(2, rating.rating)
    }

    @Test(expected = MissingChallengeException::class)
    fun `rate an unknown challenge (with challengeName)`() {
        // Given
        val principal = Principal { "user3" }

        // When
        target.rateChallenge("challenge1", principal, 2)
    }

    @Test(expected = MissingUserException::class)
    fun `rate a challenge with an unknown connected user (shouldn't never happen) (with challengeName)`() {
        // Given
        val principal = Principal { "user3" }

        challengeService.newChallenge("challenge1", "desc", Date(), "doom", null, "user", "user")

        // When
        target.rateChallenge("challenge1", principal, 2)
    }

    @Test(expected = NotARaterException::class)
    fun `rate a challenge in which we are not rater (with challengeName)`() {
        // Given
        val principal = Principal { "user" }

        challengeService.newChallenge("challenge1", "desc", Date(), "doom", null, "user", "user")

        // When
        target.rateChallenge("challenge1", principal, 2)
    }

    @Test
    fun `rate a challenge in which we are a rater (with challengeName)`() {
        // Given
        val principal = Principal { "user" }

        var newChallengeId = challengeService.newChallenge("challenge1", "desc", Date(), "doom", null, "user", "user")

        target.becomeARater("challenge1", principal)

        // When
        target.rateChallenge("challenge1", principal, 2)

        // Then
        var challenge = challengeRepository.findById(newChallengeId).get()

        Assert.assertEquals(1, challenge.raters.size)
        Assert.assertEquals("user", challenge.raters.iterator().next().name)
        Assert.assertEquals(1, challenge.ratings.size)
        val rating = challenge.ratings.iterator().next()
        Assert.assertEquals("user", rating.user.name)
        Assert.assertEquals("challenge1", rating.challenge.name)
        Assert.assertEquals(2, rating.rating)
    }
}