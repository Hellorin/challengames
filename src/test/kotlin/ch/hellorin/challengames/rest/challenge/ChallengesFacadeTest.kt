package ch.hellorin.challengames.rest.challenge

import ch.hellorin.challengames.ChallengeCreationDto
import ch.hellorin.challengames.configuration.TestConfiguration
import ch.hellorin.challengames.persistance.model.node.Game
import ch.hellorin.challengames.persistance.model.node.Role
import ch.hellorin.challengames.persistance.repository.GameRepository
import ch.hellorin.challengames.persistance.repository.RoleRepository
import ch.hellorin.challengames.service.challenge.ChallengeService
import ch.hellorin.challengames.service.user.UserService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
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
class ChallengesFacadeTest {
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

        gameRepository.save(Game("game"))
    }

    @Test
    @WithMockUser
    fun `get existing challenge`() {
        // Given
        val newChallenge = challengeService.newChallenge("challenge", "desc", Date(), "game", null, "user", "user")

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/challenges/{id}", newChallenge)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").isNotEmpty)
                .andExpect(jsonPath("$.name").value("challenge"))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.game").isNotEmpty)
                .andExpect(jsonPath("$.game.name").value("game"))
                .andExpect(jsonPath("$.game.description").value("game"))
                .andExpect(jsonPath("$.raters").isArray)
                .andExpect(jsonPath("$.raters").isEmpty)
                .andExpect(jsonPath("$.ratings").isArray)
                .andExpect(jsonPath("$.ratings").isEmpty)
                .andExpect(jsonPath("$.comments").isArray)
                .andExpect(jsonPath("$.comments").isEmpty)
    }

    @Test
    @WithMockUser
    fun `get inexisting challenge`() {
        // Given

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/challenges/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser
    fun `get most recent challenges`() {
        // Given
        challengeService.newChallenge("challenge1", "desc1", Date(), "game", null, "user","user")
        challengeService.newChallenge("challenge2", "desc2", Date(), "game", null, "user","user")
        challengeService.newChallenge("challenge3", "desc3", Date(), "game", null, "user","user")

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/challenges")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("[0].id").isNotEmpty)
                .andExpect(jsonPath("[?(@.name == 'challenge3')].name").value("challenge3"))
                .andExpect(jsonPath("[?(@.name == 'challenge3')].description").value("desc3"))
                .andExpect(jsonPath("[?(@.name == 'challenge3')].status").value("OPEN"))
                .andExpect(jsonPath("[?(@.name == 'challenge3')].game").isNotEmpty)
                .andExpect(jsonPath("[?(@.name == 'challenge3')].game.name").value("game"))
                .andExpect(jsonPath("[?(@.name == 'challenge3')].game.description").value("game"))
                .andExpect(jsonPath("[?(@.name == 'challenge3')].raters").isArray)
                .andExpect(jsonPath("[?(@.name == 'challenge3')].ratings").isArray)
                .andExpect(jsonPath("[?(@.name == 'challenge3')].comments").isArray)

                .andExpect(jsonPath("[1].id").isNotEmpty)
                .andExpect(jsonPath("[?(@.name == 'challenge2')].name").value("challenge2"))
                .andExpect(jsonPath("[?(@.name == 'challenge2')].description").value("desc2"))
                .andExpect(jsonPath("[?(@.name == 'challenge2')].status").value("OPEN"))
                .andExpect(jsonPath("[?(@.name == 'challenge2')].game").isNotEmpty)
                .andExpect(jsonPath("[?(@.name == 'challenge2')].game.name").value("game"))
                .andExpect(jsonPath("[?(@.name == 'challenge2')].game.description").value("game"))
                .andExpect(jsonPath("[?(@.name == 'challenge2')].raters").isArray)
                .andExpect(jsonPath("[?(@.name == 'challenge2')].ratings").isArray)
                .andExpect(jsonPath("[?(@.name == 'challenge2')].comments").isArray)

                .andExpect(jsonPath("[2].id").isNotEmpty)
                .andExpect(jsonPath("[?(@.name == 'challenge1')].name").value("challenge1"))
                .andExpect(jsonPath("[?(@.name == 'challenge1')].description").value("desc1"))
                .andExpect(jsonPath("[?(@.name == 'challenge1')].status").value("OPEN"))
                .andExpect(jsonPath("[?(@.name == 'challenge1')].game").isNotEmpty)
                .andExpect(jsonPath("[?(@.name == 'challenge1')].game.name").value("game"))
                .andExpect(jsonPath("[?(@.name == 'challenge1')].game.description").value("game"))
                .andExpect(jsonPath("[?(@.name == 'challenge1')].raters").isArray)
                .andExpect(jsonPath("[?(@.name == 'challenge1')].ratings").isArray)
                .andExpect(jsonPath("[?(@.name == 'challenge1')].comments").isArray)
    }

    @Test
    @WithMockUser
    fun `create a new challenge`() {
        val string = jacksonObjectMapper().writeValueAsString(ChallengeCreationDto("My Challenge1",
                "desc1",
                null,
                "user",
                "user",
                "game",
                null))

        // When/Then
        val andExpect = mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/challenges")
                        .content(string)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated)

        val challenge = challengeService.getChallenge("My Challenge1")
        andExpect
                .andExpect(redirectedUrl("/challenges/" + challenge.id))
    }

    @Test
    @WithMockUser
    fun `try to create a new challenge that already exists`() {
        // Given
        val challengeCreationDto = ChallengeCreationDto("My Challenge1",
                "desc1",
                null,
                "user",
                "user",
                "game",
                null)

        val newChallengeId = challengeService.newChallenge(
                challengeCreationDto.name,
                challengeCreationDto.description,
                Date(),
                challengeCreationDto.gameName,
                null, "user",
                "user")

        val string = jacksonObjectMapper().writeValueAsString(challengeCreationDto)

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/challenges")
                        .content(string)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isSeeOther)
                .andExpect(redirectedUrl("/challenges/$newChallengeId"))
    }

    @Test
    @WithMockUser
    fun `create a new challenge with a game that the system cannot determine`() {
        val string = jacksonObjectMapper().writeValueAsString(ChallengeCreationDto("My Challenge1",
                "desc1",
                null,
                "user",
                "user",
                "Super Mario 64",
                null))

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/challenges")
                        .content(string)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().`is`(300))
                .andExpect(jsonPath("$.gamesSummary").exists())
                .andExpect(jsonPath("$.gamesSummary.length()").value(3))
                .andExpect(jsonPath("$.gamesSummary[0].id").exists())
                .andExpect(jsonPath("$.gamesSummary[?(@.name == 'Super Mario 64')].name").value("Super Mario 64"))
                .andExpect(jsonPath("$.gamesSummary[?(@.name == 'Super Mario 64')].summary").exists())
                .andExpect(jsonPath("$.gamesSummary[1].id").exists())
                .andExpect(jsonPath("$.gamesSummary[?(@.name == 'Super Mario 64 DS')].name").value("Super Mario 64 DS"))
                .andExpect(jsonPath("$.gamesSummary[?(@.name == 'Super Mario 64 DS')].summary").exists())
                .andExpect(jsonPath("$.gamesSummary[2].id").exists())
                .andExpect(jsonPath("$.gamesSummary[?(@.name == 'Super Mario 64 Online')].name").value("Super Mario 64 Online"))
                .andExpect(jsonPath("$.gamesSummary[?(@.name == 'Super Mario 64 Online')].summary").exists())
    }

    @Test
    @WithMockUser
    fun `create a new challenge with a game id`() {
        val string = jacksonObjectMapper().writeValueAsString(ChallengeCreationDto("My Challenge1",
                "desc1",
                null,
                "user",
                "user",
                "Super Mario 64",
                1074))

        // When/Then
        val andExpect = mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/challenges")
                        .content(string)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print
                ())
                .andExpect(status().isCreated)

        val challenge = challengeService.getChallenge("My Challenge1")

        andExpect
                .andExpect(redirectedUrl("/challenges/" + challenge.id))
    }
}