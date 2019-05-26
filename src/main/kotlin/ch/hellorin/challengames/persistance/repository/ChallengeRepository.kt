package ch.hellorin.challengames.persistance.repository

import ch.hellorin.challengames.persistance.model.node.Challenge
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param

interface ChallengeRepository : Neo4jRepository<Challenge, Long> {
    fun findByName(name: String): Challenge?

    @Query("""
        MATCH (u:User {name:{username}})-[r:IS_INVOLVED_IN]->(c:Challenge)-[r2:SUBMITTED_BY]->(u2: User),(c:Challenge)-[r3:CONCERNS_GAME]->(g:Game)
        return c, r, u, r2, u2, r3, g
        """)
    fun findAllChallengesForUser(@Param("username") username: String) : List<Challenge>

    @Query("""
        MATCH (u:User {name:{username}})<-[r:SUBMITTED_BY]-(c:Challenge)<-[r2:IS_INVOLVED_IN]-(u2:User),(c:Challenge)-[r3:CONCERNS_GAME]->(g:Game)
        return c, r, u, r2, u2, r3, g
        """)
    fun findAllChallengesUserMade(@Param("username") username: String) : List<Challenge>
}