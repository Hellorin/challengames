package ch.hellorin.challengames.rest.rating

import ch.hellorin.challengames.configuration.TestConfiguration
import ch.hellorin.challengames.persistance.model.node.Game
import ch.hellorin.challengames.persistance.model.node.Role
import ch.hellorin.challengames.persistance.repository.GameRepository
import ch.hellorin.challengames.persistance.repository.RoleRepository
import ch.hellorin.challengames.service.challenge.ChallengeService
import ch.hellorin.challengames.service.rating.IRatingService
import ch.hellorin.challengames.service.user.UserService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import java.security.Principal
import java.util.*

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = [
    TestConfiguration::class
])
@ActiveProfiles("integrationTest")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class RatingFacadeTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var challengeService: ChallengeService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var gameRepository: GameRepository

    @Autowired
    lateinit var ratingService: IRatingService

    @Before
    fun setup() {
        roleRepository.save(Role("User"))

        userService.addUser("user", null, "password", listOf("User"))
        userService.addUser("user2", null, "password", listOf("User"))

        gameRepository.save(Game("game"))

        //challengeService.newChallenge("challenge1",)
    }

    @Test
    @WithMockUser
    fun `try to become a raters on a known challenge`() {
        // Given
        val newChallengeId = challengeService.newChallenge("Challenge1",
                "desc",
                Date(),
                "game",
                null,
                "user",
                "user")

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/challenges/{id}/raters", newChallengeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)

        val challenge = challengeService.getChallenge("Challenge1")
        Assert.assertEquals(1, challenge.raters.size)
        Assert.assertEquals("user", challenge.raters.iterator().next().name)

    }

    @Test
    @WithMockUser
    fun `try to become a raters on an unknown challenges`() {
        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/challenges/{id}/raters", 400)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    @WithMockUser
    fun `rate a challenge`() {
        // Given
        val newChallengeId = challengeService.newChallenge("Challenge1",
                "desc",
                Date(),
                "game",
                null,
                "user",
                "user")

        ratingService.becomeARater(newChallengeId, Principal {"user"})

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/challenges/{id}/ratings?nbStars=1", newChallengeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)

        val challenge = challengeService.getChallenge("Challenge1")
        Assert.assertEquals(1, challenge.ratings.size)
        val rating = challenge.ratings.iterator().next()
        Assert.assertEquals("user", rating.user.name)
        Assert.assertEquals("Challenge1", rating.challenge.name)
        Assert.assertEquals(1, rating.rating)
    }

    @Test
    @WithMockUser
    fun `rate a challenge with raters not added`() {
        // Given
        val newChallengeId = challengeService.newChallenge("Challenge1",
                "desc",
                Date(),
                "game",
                null,
                "user",
                "user")

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/challenges/{id}/ratings?nbStars=1", newChallengeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    @WithMockUser
    fun `rate a unknown challenge`() {
        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/challenges/{id}/ratings?nbStars=1", 400)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

}