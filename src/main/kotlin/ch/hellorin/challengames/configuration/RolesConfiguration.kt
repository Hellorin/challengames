package ch.hellorin.challengames.configuration

import ch.hellorin.challengames.persistance.model.node.Role
import ch.hellorin.challengames.persistance.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Configuration

@Configuration
class RolesConfiguration : CommandLineRunner {
    @Autowired
    lateinit var roleRepository: RoleRepository

    override fun run(vararg args: String?) {
        if (roleRepository.findByName("User") == null) {
            roleRepository.save(Role("User"))
        }

        if (roleRepository.findByName("Admin") == null) {
            roleRepository.save(Role("Admin"))
        }

    }
}