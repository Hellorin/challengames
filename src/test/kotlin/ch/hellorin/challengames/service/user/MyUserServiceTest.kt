package ch.hellorin.challengames.service.user

import ch.hellorin.challengames.configuration.ProviderConfiguration
import ch.hellorin.challengames.configuration.TestConfiguration
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
class MyUserServiceTest {
    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var myUserService: IMyUserService

    lateinit var principal: Principal

    @Test
    fun `get my user info`() {
        // Given
        principal = Principal { "user" }

        userService.addUser("user", "email", "pwd", listOf("User"))

        // When
        var myUserInfo = myUserService.myUserInfo(principal)

        // Then
        Assert.assertNotNull(myUserInfo)
        Assert.assertEquals("user", myUserInfo.name)
        Assert.assertEquals("email", myUserInfo.email)
        Assert.assertNotNull(myUserInfo.credential)
        Assert.assertNotEquals("pwd", myUserInfo.credential)
    }
}