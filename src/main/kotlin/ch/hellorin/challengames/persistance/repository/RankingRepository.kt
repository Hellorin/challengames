package ch.hellorin.challengames.persistance.repository

import ch.hellorin.challengames.persistance.model.RankingData
import org.springframework.data.neo4j.annotation.Query

interface RankingRepository {
    @Query("""
        MATCH (c:Challenge)
        RETURN count(c) as nbChallenges
    """)
    fun getRankingData(): RankingData
}