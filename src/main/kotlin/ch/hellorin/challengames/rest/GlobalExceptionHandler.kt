package ch.hellorin.challengames.rest

import ch.hellorin.challengames.ChallengeCreationError
import ch.hellorin.challengames.GameSummary
import ch.hellorin.challengames.exception.ExistingChallengeException
import ch.hellorin.challengames.exception.MissingChallengeException
import ch.hellorin.challengames.exception.MissingUserException
import ch.hellorin.challengames.exception.NotARaterException
import ch.hellorin.challengames.service.provider.CannotChooseGameException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.net.URI

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [MissingChallengeException::class, MissingUserException::class])
    fun handleException(ex: Exception, request: WebRequest) : ResponseEntity<Any> {
        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler(value = [ExistingChallengeException::class])
    fun handleExistingChallengeException(ex: ExistingChallengeException, request: WebRequest) : ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(URI.create("/challenges/" + ex.challengeId)).build()
    }

    @ExceptionHandler(value = [CannotChooseGameException::class])
    fun handleCannotChooseGameException(ex: CannotChooseGameException, request: WebRequest) : ResponseEntity<Any> {
        return ResponseEntity
                .status(300)
                .body(
                        ChallengeCreationError(
                                ex.gameData
                                        .map { GameSummary(it.first, it.second, it.third) }
                                        .toList()
                        )
                )
    }

    @ExceptionHandler(value = [NotARaterException::class])
    fun handleNotARaterException(ex: NotARaterException, request: WebRequest) : ResponseEntity<Any> {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .build()
    }
}
