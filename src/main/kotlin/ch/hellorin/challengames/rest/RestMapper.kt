package ch.hellorin.challengames.rest

import ch.hellorin.challengames.*
import ch.hellorin.challengames.persistance.model.node.Challenge
import ch.hellorin.challengames.persistance.model.node.Game
import ch.hellorin.challengames.persistance.model.node.User
import ch.hellorin.challengames.persistance.model.relationship.Rating
import org.springframework.stereotype.Component

@Component
class FacadeMapper {
    fun mapBusinessToDto(challenge: Challenge): ChallengeDto {
        return ChallengeDto(
                id = challenge.id!!,
                name = challenge.name,
                description = challenge.description,
                deadline = challenge.deadline,
                status = StatusDto.OPEN,
                totalTime = null,
                submitter = mapBusinessToDto(challenge.challenger),
                challengee = mapBusinessToDto(challenge.challengee),
                game = mapBusinessToDto(challenge.game),
                raters = listOf(),
                ratings = mapBusinessToDto(challenge.ratings),
                comments = listOf()
        )
    }

    fun mapBusinessToDtos(challenges: List<Challenge>) : List<ChallengeDto> = challenges.map { mapBusinessToDto(it) }.toList()

    fun mapBusinessToDto(player: User): PlayerDto = PlayerDto(
                id = player.id!!,
                username = player.name
        )


    fun mapBusinessToDto(game: Game): GameDto = GameDto(game.name, game.name)


    fun mapBusinessToDto(ratings: List<Rating>) : List<RatingDto> = ratings.map { RatingDto(it.rating!!, null) }
}