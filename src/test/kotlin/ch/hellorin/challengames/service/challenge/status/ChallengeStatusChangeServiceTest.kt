package ch.hellorin.challengames.service.challenge.status

import ch.hellorin.challengames.configuration.ChallengeStatusChangesConfiguration
import ch.hellorin.challengames.persistance.model.node.Challenge
import ch.hellorin.challengames.persistance.model.node.Game
import ch.hellorin.challengames.persistance.model.node.Role
import ch.hellorin.challengames.persistance.model.node.User
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import java.util.*
import org.springframework.test.context.junit4.rules.SpringMethodRule
import org.springframework.test.context.junit4.rules.SpringClassRule
import org.junit.ClassRule
import org.junit.Rule
import org.springframework.security.test.context.support.WithMockUser


@RunWith(Parameterized::class)
@ContextConfiguration(classes = [ChallengeStatusChangesConfiguration::class])
class ChallengeStatusChangeServiceTest(
        private val initialStatus: ChallengeStatus,
        private val challengeeUsername: String,
        private val challengerUsername: String,
        private val targetStatus: ChallengeStatus,
        private val expectedResult: Boolean) {

    @Rule
    @JvmField
    val springMethodRule = SpringMethodRule()

    @Autowired
    lateinit var challengeStatusChangeService: IChallengeStatusChangeService

    companion object {
        @JvmStatic
        @get:ClassRule
        val SPRING_CLASS_RULE = SpringClassRule()

        @JvmStatic
        @Parameterized.Parameters(name= "from {0} to {3} with challengeeUser={1} and challengerUser={2} -> expected {4}")
        fun data() = listOf(
                arrayOf(ChallengeStatus.OPEN, "user", "user2", ChallengeStatus.ACCEPTED, true),
                arrayOf(ChallengeStatus.OPEN, "user2", "user3", ChallengeStatus.ACCEPTED, false),
                arrayOf(ChallengeStatus.OPEN, "user", "user2", ChallengeStatus.IN_PROGRESS, true),
                arrayOf(ChallengeStatus.OPEN, "user3", "user2", ChallengeStatus.IN_PROGRESS, false),
                arrayOf(ChallengeStatus.ACCEPTED, "user", "user2", ChallengeStatus.IN_PROGRESS, true),
                arrayOf(ChallengeStatus.ACCEPTED, "user3", "user2", ChallengeStatus.IN_PROGRESS, false),
                arrayOf(ChallengeStatus.ACCEPTED, "user", "user2", ChallengeStatus.ABANDONED, true),
                arrayOf(ChallengeStatus.ACCEPTED, "user3", "user2", ChallengeStatus.ABANDONED, false),
                arrayOf(ChallengeStatus.IN_PROGRESS, "user", "user2", ChallengeStatus.ABANDONED, true),
                arrayOf(ChallengeStatus.IN_PROGRESS, "user3", "user2", ChallengeStatus.ABANDONED, false),
                arrayOf(ChallengeStatus.OPEN, "user", "user2", ChallengeStatus.DECLINED, true),
                arrayOf(ChallengeStatus.OPEN, "user3", "user2", ChallengeStatus.DECLINED, false),
                arrayOf(ChallengeStatus.IN_PROGRESS, "user", "user2", ChallengeStatus.FINISHED, true),
                arrayOf(ChallengeStatus.IN_PROGRESS, "user3", "user", ChallengeStatus.FINISHED, true),
                arrayOf(ChallengeStatus.IN_PROGRESS, "user3", "user2", ChallengeStatus.FINISHED, false),
                arrayOf(ChallengeStatus.FINISHED, "user2", "user", ChallengeStatus.COMPLETED, true),
                arrayOf(ChallengeStatus.FINISHED, "user", "user2", ChallengeStatus.COMPLETED, false)
        )
    }

    @Test
    @WithMockUser
    fun test() {
        // Given
        var challenge = Challenge(
                "challenge",
                "desc",
                Date(),
                Game("game"),
                challengee = User(challengeeUsername, "email", "pwd", listOf(Role("user"))),
                challenger = User(challengerUsername, "email", "pwd", listOf(Role("user")))
        )
        challenge.status = initialStatus

        // When
        val transitionAccepted = challengeStatusChangeService.isTransitionAccepted(challenge, targetStatus)

        // Then
        Assert.assertEquals(expectedResult, transitionAccepted)
    }
}