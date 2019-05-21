package ch.hellorin.challengames.persistance.model.node

import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.Index
import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity("Role")
class Role() {
    @Id
    @GeneratedValue
    private var id: Long? = null

    @Index
    lateinit var name: String

    constructor(name : String) : this() {
        this.name = name
    }
}