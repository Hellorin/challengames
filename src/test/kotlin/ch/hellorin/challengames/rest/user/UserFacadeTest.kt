package ch.hellorin.challengames.rest.user

import ch.hellorin.challengames.configuration.TestConfiguration
import ch.hellorin.challengames.persistance.model.node.Role
import ch.hellorin.challengames.persistance.repository.RoleRepository
import ch.hellorin.challengames.service.user.UserService
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = [
    TestConfiguration::class
])
@ActiveProfiles("integrationTest")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class UserFacadeTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var userService: UserService

    @Before
    fun setup() {
        roleRepository.save(Role("User"))
    }

    @Test
    @WithMockUser
    fun `get user that exists`() {
        // Given
        val userId = userService.addUser("user2", null, "password", listOf("User"))

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/user/{id}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.credentials").doesNotExist())
    }

    @Test
    @WithMockUser
    fun `get user that doesn't exist`() {
        // Given

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/user/{id}", 10)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound)

    }
}