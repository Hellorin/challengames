package ch.hellorin.challengames.persistance.model.relationship

import ch.hellorin.challengames.persistance.model.node.Challenge
import ch.hellorin.challengames.persistance.model.node.User
import org.neo4j.ogm.annotation.EndNode
import org.neo4j.ogm.annotation.RelationshipEntity
import org.neo4j.ogm.annotation.StartNode

@RelationshipEntity(type="RATED")
class Rating() {
    @StartNode
    lateinit var user: User

    @EndNode
    lateinit var challenge: Challenge

    var rating: Int? = null

    constructor(user : User, challenge: Challenge, rating: Int) : this() {
        this.user = user
        this.challenge = challenge
        this.rating = rating
    }
}