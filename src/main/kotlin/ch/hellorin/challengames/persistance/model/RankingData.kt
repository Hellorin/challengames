package ch.hellorin.challengames.persistance.model

import org.springframework.data.neo4j.annotation.QueryResult

@QueryResult
class RankingData {
    var nbChallenges: Int = 0
}