package ch.hellorin.challengames.service.rating

import ch.hellorin.challengames.exception.MissingChallengeException
import ch.hellorin.challengames.exception.MissingUserException
import ch.hellorin.challengames.exception.NotARaterException
import ch.hellorin.challengames.persistance.model.node.Challenge
import ch.hellorin.challengames.service.challenge.IChallengeAdminService
import ch.hellorin.challengames.service.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal

interface IRatingService {
    fun becomeARater(id: Long, principal: Principal)

    fun becomeARater(challengeName: String, principal: Principal)

    fun rateChallenge(challengeName: String, principal: Principal, nbStars: Int)

    fun rateChallenge(id: Long, principal: Principal, nbStars: Int)
}

@Service
@Transactional
class RatingService(
        private val challengeService: IChallengeAdminService,
        private val userService: UserService) : IRatingService {

    @Throws(MissingChallengeException::class, MissingUserException::class)
    override fun becomeARater(challengeName: String, principal: Principal) {
        var challenge = challengeService.getChallenge(challengeName)
        becomeARater(challenge, principal)
    }

    @Throws(MissingChallengeException::class, MissingUserException::class)
    override fun becomeARater(id: Long, principal: Principal) {
        var challenge = challengeService.getChallengeById(id)
        becomeARater(challenge, principal)
    }

    private fun becomeARater(challenge: Challenge, principal: Principal) {
        var user = userService.getUser(principal.name)

        challenge.addRater(user)
    }

    @Throws(MissingChallengeException::class, MissingUserException::class, NotARaterException::class)
    override fun rateChallenge(challengeName: String, principal: Principal, nbStars: Int) {
        var challenge = challengeService.getChallenge(challengeName)
        rateChallenge(challenge, principal, nbStars)
    }

    @Throws(MissingChallengeException::class, MissingUserException::class, NotARaterException::class)
    override fun rateChallenge(id: Long, principal: Principal, nbStars: Int) {
        var challenge = challengeService.getChallengeById(id)
        rateChallenge(challenge, principal, nbStars)
    }

    private fun rateChallenge(challenge: Challenge, principal: Principal, nbStars: Int) {
        val user = userService.getUser(principal.name)

        if (challenge.raters.find { it.name == user.name } != null) {
            challenge.addRating(user, nbStars)
        } else {
            throw NotARaterException()
        }
    }
}