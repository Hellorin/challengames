package ch.hellorin.challengames.persistance.repository

import ch.hellorin.challengames.persistance.model.node.Role
import org.springframework.data.repository.CrudRepository

interface RoleRepository : CrudRepository<Role, Long> {
    fun findByName(name : String) : Role?
}