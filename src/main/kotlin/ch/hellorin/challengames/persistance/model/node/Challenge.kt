package ch.hellorin.challengames.persistance.model.node

import ch.hellorin.challengames.persistance.model.relationship.Rating
import ch.hellorin.challengames.service.challenge.status.ChallengeStatus
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.Convert
import org.neo4j.ogm.annotation.typeconversion.DateLong
import java.util.*

@NodeEntity
class Challenge() {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Index
    lateinit var name: String

    lateinit var description: String

    @DateLong
    var deadline: Date? = Date()

    @Convert(ChallengeStatusConverter::class)
    var status: ChallengeStatus = ChallengeStatus.OPEN

    @Relationship(type = "CONCERNS_GAME", direction = Relationship.OUTGOING)
    lateinit var game: Game

    @Relationship(type = "IS_INVOLVED_IN", direction = Relationship.INCOMING)
    lateinit var challengee: User

    @Relationship(type = "SUBMITTED_BY", direction = Relationship.OUTGOING)
    lateinit var challenger: User

    @Relationship(type = "CAN_RATE", direction = Relationship.INCOMING)
    var raters: MutableSet<User> = mutableSetOf()

    @Relationship(type = "RATED", direction = Relationship.INCOMING)
    var ratings: MutableList<Rating> = mutableListOf()

    constructor(name: String,
                description: String,
                deadline: Date? = Date(),
                game: Game,
                challengee: User,
                challenger: User) : this() {
        this.name = name
        this.description = description
        this.deadline = deadline
        this.game = game
        this.challengee = challengee
        this.challenger = challenger
    }

    fun addRater(user: User) {
        raters.add(user)
    }

    fun addRating(user:User, nbStars: Int) {
        ratings.add(Rating(user, this, nbStars))
    }
}