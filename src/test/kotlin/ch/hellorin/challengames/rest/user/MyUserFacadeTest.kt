package ch.hellorin.challengames.rest.user

import ch.hellorin.challengames.configuration.TestConfiguration
import ch.hellorin.challengames.persistance.model.node.Role
import ch.hellorin.challengames.persistance.repository.RoleRepository
import ch.hellorin.challengames.service.UserService
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

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = [
    TestConfiguration::class
])
@ActiveProfiles("integrationTest")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class MyUserFacadeTest {
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
    fun `get my user information (without being authenticated)`() {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/me/user/")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    @WithMockUser
    fun `get my user information`() {
        // Given
        userService.addUser("user", "email@email.com", "password", listOf("User"))

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/me/user/")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist())
    }
}