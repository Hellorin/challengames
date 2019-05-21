package ch.hellorin.challengames.rest.challenge

import ch.hellorin.challengames.ChallengeCreationDto
import ch.hellorin.challengames.ChallengeDto
import ch.hellorin.challengames.StatusDto
import ch.hellorin.challengames.rest.FacadeMapper
import ch.hellorin.challengames.service.challenge.IChallengeService
import ch.hellorin.challengames.service.challenge.status.ChallengeStatus
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.security.Principal
import javax.ws.rs.QueryParam


@RestController
@RequestMapping("/challenges")
class ChallengesFacade(val challengeService: IChallengeService, val mapper : FacadeMapper) {

    @GetMapping("/{id}", produces = ["application/json"])
    fun getChallenge(@PathVariable id : Long) : ResponseEntity<ChallengeDto?> {
        return ResponseEntity.ok(
                mapper.mapBusinessToDto(
                        challengeService.getChallengeById(id)
                )
        )
    }

    @GetMapping
    fun getMostRecentChallenges() : ResponseEntity<List<ChallengeDto>> {
        return ResponseEntity.ok(
                challengeService.getMostRecentChallenges()
                        .map { mapper.mapBusinessToDto(it) }
                        .sortedBy { it.deadline }
                        .asReversed()
        )
    }

    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createChallenge(
            @RequestBody challengeDto: ChallengeCreationDto,
            principal: Principal
    ) : ResponseEntity<Long> {
        val newChallengeId = challengeService.newChallenge(
                challengeName = challengeDto.name,
                description = challengeDto.description,
                gameName = challengeDto.gameName,
                gameId = challengeDto.gameId,
                challengeeName = challengeDto.challengeeName,
                principal = principal)
        return ResponseEntity.created(URI.create("/challenges/$newChallengeId")).body(newChallengeId)
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun changeChallengeStatus(
            @PathVariable id: Long,
            @QueryParam("status") status: StatusDto) = challengeService.changeChallengeStatus(id, ChallengeStatus.valueOf(status.name))
}