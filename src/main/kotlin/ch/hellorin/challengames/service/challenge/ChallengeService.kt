package ch.hellorin.challengames.service.challenge

import ch.hellorin.challengames.exception.ExistingChallengeException
import ch.hellorin.challengames.exception.ForbiddenStatusChangeException
import ch.hellorin.challengames.exception.MissingChallengeException
import ch.hellorin.challengames.exception.MissingUserException
import ch.hellorin.challengames.persistance.model.node.Challenge
import ch.hellorin.challengames.persistance.model.node.User
import ch.hellorin.challengames.persistance.repository.ChallengeRepository
import ch.hellorin.challengames.service.IGameService
import ch.hellorin.challengames.service.IUserService
import ch.hellorin.challengames.service.challenge.status.ChallengeStatus
import ch.hellorin.challengames.service.challenge.status.IChallengeStatusChangeService
import ch.hellorin.challengames.service.provider.CannotChooseGameException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal
import java.util.*

@Service
@Transactional
class ChallengeService(
        private val userService: IUserService,
        private val gameService: IGameService,
        private val challengeRepository: ChallengeRepository,
        private val challengeStatusChangeService: IChallengeStatusChangeService) : IChallengeAdminService, IMyChallengeService {

    private fun abstractNewChallenge(challengeName: String,
                                     description: String,
                                     deadline: Date,
                                     gameName: String,
                                     id: Long? = null,
                                     challenger: User,
                                     challengee: User): Long {
        if (!challengeExists(challengeName)) {
            var game = gameService.getGame(gameName, id)
            return challengeRepository.save(
                    Challenge(
                            name = challengeName,
                            description = description,
                            deadline = deadline,
                            game = game,
                            challengee = challengee,
                            challenger = challenger
                    )
            ).id!!
        } else {
            throw ExistingChallengeException(getChallenge(challengeName)!!.id ?: 0)
        }
    }

    @Throws(ExistingChallengeException::class, CannotChooseGameException::class)
    override fun newChallenge(
            challengeName: String,
            description: String,
            deadline: Date,
            gameName: String,
            gameId: Long?,
            challengeeName: String,
            principal: Principal): Long {
        val challengee = userService.getUser(challengeeName)
        var challenger = userService.getUser(principal.name)
        return abstractNewChallenge(
                challengeName = challengeName,
                description = description,
                deadline = deadline,
                gameName = gameName,
                id = gameId,
                challengee = challengee,
                challenger = challenger)
    }

    @Throws(ExistingChallengeException::class, CannotChooseGameException::class, MissingUserException::class)
    override fun newChallenge(challengeName: String,
                              description: String,
                              deadline: Date,
                              gameName: String,
                              gameId: Long?,
                              challengeeName: String,
                              username: String): Long {
        var challenger = userService.getUser(username)
        val challengee = userService.getUser(challengeeName)
        return abstractNewChallenge(challengeName = challengeName,
                description = description,
                deadline = deadline,
                gameName = gameName,
                id = gameId,
                challengee = challengee,
                challenger = challenger)
    }

    @Throws(MissingChallengeException::class)
    override fun getChallengeById(id: Long): Challenge {
        var findById = challengeRepository.findById(id).orElse(null)

        return findById ?: throw MissingChallengeException()
    }

    @Throws(MissingChallengeException::class)
    override fun getChallenge(challengeName: String): Challenge =
            challengeRepository.findByName(challengeName) ?: throw MissingChallengeException()

    override fun getMostRecentChallenges(): List<Challenge> = challengeRepository.findAll().take(5).toList()

    override fun challengeExists(challengeName: String): Boolean = challengeRepository.findByName(challengeName) != null

    override fun changeChallengeStatus(id: Long, newStatus: ChallengeStatus) {
        var challenge = getChallengeById(id)

        if (challengeStatusChangeService.isTransitionAccepted(challenge, newStatus)) {
            challenge.status = newStatus
        } else {
            throw ForbiddenStatusChangeException()
        }
    }

    override fun myChallenges(principal: Principal): List<Challenge> = challengeRepository.findAllChallengesForUser(principal.name)

    override fun myChallengesMade(principal: Principal): List<Challenge> = challengeRepository.findAllChallengesUserMade(principal.name)
}

interface IChallengeAdminService : IChallengeService {
    fun newChallenge(challengeName: String,
                     description: String,
                     deadline: Date = Date(),
                     gameName: String,
                     gameId: Long? = null,
                     challengeeName: String,
                     username: String): Long
}

interface IChallengeService {
    fun newChallenge(challengeName: String,
                     description: String,
                     deadline: Date = Date(),
                     gameName: String,
                     gameId: Long? = null,
                     challengeeName: String,
                     principal: Principal): Long

    fun getChallengeById(id: Long): Challenge

    fun getChallenge(challengeName: String): Challenge

    fun getMostRecentChallenges(): List<Challenge>

    fun challengeExists(challengeName: String): Boolean

    fun changeChallengeStatus(id: Long, newStatus: ChallengeStatus)
}

interface IMyChallengeService {
    fun myChallenges(principal: Principal): List<Challenge>

    fun myChallengesMade(principal: Principal): List<Challenge>
}