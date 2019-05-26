package ch.hellorin.challengames.service.game

import ch.hellorin.challengames.persistance.model.node.Game
import ch.hellorin.challengames.persistance.repository.GameRepository
import ch.hellorin.challengames.service.user.UserService
import ch.hellorin.challengames.service.provider.CannotChooseGameException
import ch.hellorin.challengames.service.provider.IGameProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GameService(private val gameRepository: GameRepository, private val gameProvider: IGameProvider) : IGameService {

    private val LOGGER : Logger = LoggerFactory.getLogger(UserService::class.java)

    @Throws(CannotChooseGameException::class)
    override fun addGame(name: String): Game = retrieveOrCreate(name)

    override fun addGame(name: String, externalId: Long) : Game = retrieveOrCreate(name, externalId)

    @Throws(CannotChooseGameException::class)
    override fun getGame(name: String, id: Long?): Game = retrieveOrCreate(name, id)

    private fun retrieveOrCreate(name: String, id: Long? = null): Game {
        var findByName = gameRepository.findByName(name)
        return findByName ?: try {
            LOGGER.info("The game {} doesn't exist in the database! Trying to get it from external source", name)
            // Get from external source
            var game = gameProvider.tryToGetGame(name, id)

            // Persist it
            gameRepository.save(Game(game.name))

        } catch (e: CannotChooseGameException) { // Cannot find or decide game from external source
            LOGGER.warn("Cannot retrieve game {} from external source !", name)
            throw e
        }
    }
}

interface IGameService {
    fun addGame(name: String): Game

    fun addGame(name: String, externalId: Long) : Game

    fun getGame(name: String, id: Long? = null): Game
}