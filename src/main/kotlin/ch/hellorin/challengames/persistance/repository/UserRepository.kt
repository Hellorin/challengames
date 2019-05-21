package ch.hellorin.challengames.persistance.repository

import ch.hellorin.challengames.persistance.model.node.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByName(name: String) : User?
}