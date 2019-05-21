package ch.hellorin.challengames.persistance.model.node

import ch.hellorin.challengames.service.challenge.status.ChallengeStatus
import org.neo4j.ogm.typeconversion.AttributeConverter

class ChallengeStatusConverter : AttributeConverter<ChallengeStatus, String> {
    override fun toGraphProperty(p0: ChallengeStatus?): String? = p0?.name

    override fun toEntityAttribute(p0: String?): ChallengeStatus? = if (p0 != null) ChallengeStatus.valueOf(p0) else null
}