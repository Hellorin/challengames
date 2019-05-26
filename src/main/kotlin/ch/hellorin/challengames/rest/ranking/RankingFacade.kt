package ch.hellorin.challengames.rest.ranking

import ch.hellorin.challengames.RankingDataDto
import ch.hellorin.challengames.service.IRankingService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ranking")
class RankingFacade(private val rankingService: IRankingService) {

    @GetMapping(produces = ["application/json"])
    @ResponseStatus(HttpStatus.OK)
    fun getRanking() : RankingDataDto = rankingService.getRanking().let { RankingDataDto(it.nbChallenges) }

}