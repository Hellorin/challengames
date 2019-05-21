package ch.hellorin.challengames.persistance.model.node

import org.neo4j.ogm.annotation.*

@NodeEntity("User")
class User() {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Index
    lateinit var name: String

    var email: String? = null

    lateinit var credential: String

    @Relationship(type = "PLAYS_ROLE", direction = Relationship.OUTGOING)
    lateinit var roles: List<Role>

    constructor(name: String,
                email: String?,
                credential: String,
                roles: List<Role>) : this() {
        this.name = name
        this.email = email
        this.credential = credential
        this.roles = roles
    }

    override fun equals(other: Any?) : Boolean {
        return if (other == null) {
            false
        } else {
            if (other is User) {
                this.name == other.name && this.email == other.email
            } else {
                false
            }
        }

    }
}