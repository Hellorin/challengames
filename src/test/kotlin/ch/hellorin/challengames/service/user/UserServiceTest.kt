package ch.hellorin.challengames.service.user

import ch.hellorin.challengames.configuration.ProviderConfiguration
import ch.hellorin.challengames.configuration.TestConfiguration
import ch.hellorin.challengames.exception.ExistingUserException
import ch.hellorin.challengames.exception.MissingUserException
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import java.security.Principal

@ContextConfiguration(classes = [
    TestConfiguration::class
])
@DataNeo4jTest(excludeAutoConfiguration = [ProviderConfiguration::class])
@ActiveProfiles("localtest")
@RunWith(SpringRunner::class)
class UserServiceTest {

    @Autowired
    lateinit var userService: IUserService

    lateinit var principal : Principal

    @Test
    fun `add new user`() {
        // Given

        // When
        val id = userService.addUser("username", "email", "password", listOf("User"))

        // Then
        val userById = userService.getUserById(id)

        Assert.assertNotNull(userById)
        Assert.assertEquals("username", userById.name)
        Assert.assertEquals("email", userById.email)
        Assert.assertNotNull(userById.credential)
        Assert.assertNotEquals("password", userById.credential)
    }

    @Test(expected = ExistingUserException::class)
    fun `add new user that doesn't exist`() {
        // Given
        userService.addUser("username", "email", "password", listOf("User"))

        // When
        userService.addUser("username", "email", "password", listOf("User"))

        // Then
    }

    @Test
    fun `get existing user`() {
        // Given
        userService.addUser("username", "email", "password", listOf("User"))

        // When
        val user = userService.getUser("username")

        // Then
        Assert.assertNotNull(user)
        Assert.assertEquals("username", user.name)
        Assert.assertEquals("email", user.email)
        Assert.assertNotNull(user.credential)
        Assert.assertNotEquals("password", user.credential)
    }

    @Test(expected = MissingUserException::class)
    fun `get inexisting user`() {
        // Given

        // When
        val user = userService.getUser("username")

        // Then
    }



}