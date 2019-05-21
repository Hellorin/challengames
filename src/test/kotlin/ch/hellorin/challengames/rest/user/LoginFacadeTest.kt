package ch.hellorin.challengames.rest.user

import ch.hellorin.challengames.configuration.TestConfiguration
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
class LoginFacadeTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser
    fun `login with a user`() {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/login/")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
    }

    @Test
    fun `login no user`() {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/login/")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser
    fun `test is connected with a connected user`() {
        // Given
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/login/")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/login/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
                .contains("true")
    }

    @Test
    @WithMockUser
    fun `test is connected with a not connected user`() {
        // Given

        // When/Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/login/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
                .contains("false")
    }
}