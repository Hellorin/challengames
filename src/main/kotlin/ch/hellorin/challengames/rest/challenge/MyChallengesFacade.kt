package ch.hellorin.challengames.rest.challenge

import ch.hellorin.challengames.ChallengeDto
import ch.hellorin.challengames.rest.FacadeMapper
import ch.hellorin.challengames.service.challenge.IMyChallengeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/me/challenges")
class MyChallengesFacade(val challengeService: IMyChallengeService, val mapper: FacadeMapper) {

    @GetMapping(produces = ["application/json"])
    fun myChallenges(@RequestParam origin: ChallengeOrigin, principal: Principal): ResponseEntity<List<ChallengeDto>> {
        return ResponseEntity.ok(
                mapper.mapBusinessToDtos(
                        if (ChallengeOrigin.CHALLENGEE == origin)
                            challengeService.myChallenges(principal)
                        else
                            challengeService.myChallengesMade(principal)
                )
        )
    }
}