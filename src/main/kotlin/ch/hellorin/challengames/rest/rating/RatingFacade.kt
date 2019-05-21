package ch.hellorin.challengames.rest.rating

import ch.hellorin.challengames.service.IRatingService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/challenges")
class RatingFacade(private val ratingService: IRatingService) {

    @PutMapping("/{challengeId}/raters", produces = ["application/json"])
    @ResponseStatus(HttpStatus.OK)
    fun becomeARater(@PathVariable challengeId : Long, principal: Principal) {
        ratingService.becomeARater(challengeId, principal)
    }

    @PostMapping("/{challengeId}/ratings", produces = ["application/json"])
    @ResponseStatus(HttpStatus.OK)
    fun rate(@PathVariable challengeId : Long, @RequestParam nbStars: Int, principal: Principal) {
        ratingService.rateChallenge(challengeId, principal, nbStars)
    }
}