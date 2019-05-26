package ch.hellorin.challengames.service.ranking

import ch.hellorin.challengames.persistance.model.RankingData
import ch.hellorin.challengames.persistance.repository.RankingRepository
import org.springframework.stereotype.Service

interface IRankingService {
    fun getRanking() : RankingData
}

@Service
class RankingService(private val rankingRepository: RankingRepository) : IRankingService {
    override fun getRanking() : RankingData = rankingRepository.getRankingData()
}