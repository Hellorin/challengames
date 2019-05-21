package ch.hellorin.challengames.exception

class ExistingChallengeException(val challengeId: Long) : Exception()

class ExistingUserException : Exception()