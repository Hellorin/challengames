package ch.hellorin.challengames.rest.challenge

import ch.hellorin.challengames.configuration.TestConfiguration
import ch.hellorin.challengames.persistance.model.node.Game
import ch.hellorin.challengames.persistance.model.node.Role
import ch.hellorin.challengames.persistance.repository.GameRepository
import ch.hellorin.challengames.persistance.repository.RoleRepository
import ch.hellorin.challengames.service.challenge.ChallengeService
import ch.hellorin.challengames.service.UserService
import org.junit.Before
import org.junit.Ignore
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
import java.util.*

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = [
    TestConfiguration::class
])
@ActiveProfiles("integrationTest")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class MyChallengesFacadeTest {
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

    @Before
    fun setup() {
        roleRepository.save(Role("User"))

        userService.addUser("user", null, "password", listOf("User"))
        userService.addUser("user2", null, "password", listOf("User"))

        gameRepository.save(Game("game"))
    }

    @Test
    @WithMockUser
    fun `get my challenge for which I am the challengee`() {
        // Given
        challengeService.newChallenge(
                "challenge1",
                "desc1",
                Date(),
                "game",
                null,
                "user",
                "user")

        challengeService.newChallenge(
                "challenge2",
                "desc2",
                Date(),
                "game",
                null,
                "user",
                "user")

        challengeService.newChallenge(
                "challenge3",
                "desc3",
                Date(),
                "game",
                null,
                "user2",
                "user")

        challengeService.newChallenge(
                "challenge4",
                "desc4",
                Date(),
                "game",
                null,
                "user",
                "user")

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/me/challenges?origin=CHALLENGEE")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))

                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge4')].id").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge4')].name").value("challenge4"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge4')].description").value("desc4"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge4')].status").value("OPEN"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge4')].game").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge4')].game.name").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge4')].game.description").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge4')].raters").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge4')].ratings").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge4')].comments").isArray)

                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].id").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].name").value("challenge2"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].description").value("desc2"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].status").value("OPEN"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].game").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].game.name").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].game.description").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].raters").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].ratings").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].comments").isArray)

                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].id").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].name").value("challenge1"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].description").value("desc1"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].status").value("OPEN"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].game").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].game.name").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].game.description").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].raters").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].ratings").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].comments").isArray)
    }

    @Test
    @WithMockUser
    fun `get my challenge for which I am the challenger`() {
        // Given
        challengeService.newChallenge(
                "challenge1",
                "desc1",
                Date(),
                "game",
                null,
                "user2",
                "user")

        challengeService.newChallenge(
                "challenge2",
                "desc2",
                Date(),
                "game",
                null,
                "user2",
                "user")

        challengeService.newChallenge(
                "challenge3",
                "desc3",
                Date(),
                "game",
                null,
                "user2",
                "user")

        challengeService.newChallenge(
                "challenge4",
                "desc4",
                Date(),
                "game",
                null,
                "user2",
                "user2")

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/me/challenges?origin=CHALLENGER")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))

                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].id").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].name").value("challenge1"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].description").value("desc1"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].status").value("OPEN"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].game").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].game.name").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].game.description").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].raters").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].ratings").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge1')].comments").isArray)

                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].id").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].name").value("challenge2"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].description").value("desc2"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].status").value("OPEN"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].game").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].game.name").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].game.description").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].raters").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].ratings").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge2')].comments").isArray)

                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge3')].id").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge3')].name").value("challenge3"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge3')].description").value("desc3"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge3')].status").value("OPEN"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge3')].game").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge3')].game.name").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge3')].game.description").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge3')].raters").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge3')].ratings").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("[?(@.name == 'challenge3')].comments").isArray)


    }
}