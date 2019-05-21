package ch.hellorin.challengames.persistance.repository

import ch.hellorin.challengames.persistance.model.node.Game
import org.springframework.data.repository.CrudRepository

interface GameRepository : CrudRepository<Game, Long> {
    fun findByName(name: String): Game?
}