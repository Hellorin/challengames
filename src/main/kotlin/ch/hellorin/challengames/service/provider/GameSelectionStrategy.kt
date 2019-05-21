package ch.hellorin.challengames.service.provider

import ch.hellorin.challengames.exception.MissingGameException
import org.springframework.stereotype.Component

interface GameSelectionStrategy {
    fun select(games: Array<GameDataDto>) : GameDataDto
}

@Component("oneResult")
class OneResultGameSelectionStrategy : GameSelectionStrategy {
    override fun select(games: Array<GameDataDto>) : GameDataDto {
        return when (games.size) {
            0 -> throw MissingGameException()
            1 -> games.first()
            else -> throw CannotChooseGameException(games.map { Triple(it.id, it.name, it.summary )})
        }
    }
}