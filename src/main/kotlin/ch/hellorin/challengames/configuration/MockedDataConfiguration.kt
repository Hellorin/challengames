package ch.hellorin.challengames.configuration

import ch.hellorin.challengames.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dev")
class MockedDataConfiguration : CommandLineRunner {
    @Autowired
    lateinit var userService: IUserService

    override fun run(vararg args: String?) {
        try {
            userService.addUser("user", "user@email.com", "password", listOf("User"))
        } catch(e : Throwable) { }

        try {
            userService.addUser("technical1", null, "technical1", listOf("Technical"))
        } catch(e : Throwable) {
            throw e
        }
    }

}