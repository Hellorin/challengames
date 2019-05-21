package ch.hellorin.challengames.persistance.repository

import ch.hellorin.challengames.persistance.model.node.Challenge
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param

interface ChallengeRepository : Neo4jRepository<Challenge, Long> {
    fun findByName(name: String): Challenge?

    @Query("MATCH (Player {name:{username}})-[IS_INVOLVED_IN]->(c:Challenge) return c")
    fun findAllChallengesForUser(@Param("username") username: String) : List<Challenge>

    @Query("MATCH (Player {name:{username}})<-[SUBMITTED_BY]-(c:Challenge) return c")
    fun findAllChallengesUserMade(@Param("username") username: String) : List<Challenge>
}