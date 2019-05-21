package ch.hellorin.challengames.service.challenge.status

import ch.hellorin.challengames.persistance.model.node.Challenge
import org.kie.api.runtime.KieContainer
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

interface IChallengeStatusChangeService {
    fun isTransitionAccepted(challenge: Challenge, targetStatus: ChallengeStatus) : Boolean
}

@Service
class ChallengeStatusChangeService(private val kContainer: KieContainer) : IChallengeStatusChangeService {
    override fun isTransitionAccepted(challenge: Challenge, targetStatus: ChallengeStatus) : Boolean {
        var name = SecurityContextHolder.getContext().authentication.name
        val input = InitialChallengeStatus(
                challenge.status,
                targetStatus,
                changeByChallenger = challenge.challenger.name == name,
                changeByChallengee = challenge.challengee.name == name)

        var newKieSession = kContainer.newKieSession()

        var finalChallengeStatus = FinalChallengeStatus()
        newKieSession.setGlobal("finalStatus", finalChallengeStatus)
        newKieSession.insert(input)
        newKieSession.fireAllRules()
        newKieSession.dispose()

        return finalChallengeStatus.isAuthorizedChallengeChange
    }

}