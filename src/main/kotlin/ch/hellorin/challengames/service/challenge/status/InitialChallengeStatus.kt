package ch.hellorin.challengames.service.challenge.status

class InitialChallengeStatus(
        @get:JvmName("initialStatus")
        val initialStatus : ChallengeStatus,

        @get:JvmName("targetStatus")
        val targetStatus: ChallengeStatus,

        @get:JvmName("changeByChallenger")
        var changeByChallenger: Boolean,

        @get:JvmName("changeByChallengee")
        var changeByChallengee: Boolean)