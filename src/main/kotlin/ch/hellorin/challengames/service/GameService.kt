package ch.hellorin.challengames.service

import ch.hellorin.challengames.persistance.model.node.Game
import ch.hellorin.challengames.persistance.repository.GameRepository
import ch.hellorin.challengames.service.provider.CannotChooseGameException
import ch.hellorin.challengames.service.provider.IGameProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GameService(private val gameRepository: GameRepository, private val gameProvider: IGameProvider) : IGameService {

    @Throws(CannotChooseGameException::class)
    override fun addGame(name: String): Game = retrieveOrCreate(name)

    override fun addGame(name: String, externalId: Long) : Game = retrieveOrCreate(name, externalId)

    @Throws(CannotChooseGameException::class)
    override fun getGame(name: String, id: Long?): Game = retrieveOrCreate(name, id)

    private fun retrieveOrCreate(name: String, id: Long? = null): Game {
        var findByName = gameRepository.findByName(name)
        return findByName ?: try {
            // Get from external source
            var game = gameProvider.tryToGetGame(name, id)

            // Persist it
            gameRepository.save(Game(game.name))

        } catch (e: CannotChooseGameException) { // Cannot find or decide game from external source
            throw e
        }
    }
}

interface IGameService {
    fun addGame(name: String): Game

    fun addGame(name: String, externalId: Long) : Game

    fun getGame(name: String, id: Long? = null): Game
}