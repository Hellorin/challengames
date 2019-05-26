package ch.hellorin.challengames.rest.ranking

import ch.hellorin.challengames.configuration.TestConfiguration
import ch.hellorin.challengames.persistance.model.node.Game
import ch.hellorin.challengames.persistance.model.node.Role
import ch.hellorin.challengames.persistance.repository.GameRepository
import ch.hellorin.challengames.persistance.repository.RoleRepository
import ch.hellorin.challengames.service.UserService
import ch.hellorin.challengames.service.challenge.ChallengeService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = [
    TestConfiguration::class
])
@ActiveProfiles("integrationTest")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class RankingFacadeTest {
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

        userService.addUser("user1", null, "password", listOf("User"))
        userService.addUser("user2", null, "password", listOf("User"))

        gameRepository.save(Game("Doom"))
    }

    @Test
    fun `get ranking`() {
        // Given
        challengeService.newChallenge(challengeName = "Challenge1", description = "desc", gameName = "Doom", gameId= null, challengeeName="user1", username = "user2")
        challengeService.newChallenge(challengeName = "Challenge2", description = "desc", gameName = "Doom", gameId= null, challengeeName="user2", username = "user1")

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/ranking")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nbChallenges").value(2))
    }
}