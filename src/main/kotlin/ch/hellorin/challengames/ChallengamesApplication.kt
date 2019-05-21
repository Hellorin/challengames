package ch.hellorin.challengames

import ch.hellorin.challengames.configuration.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans
import org.springframework.context.annotation.Import
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories

@SpringBootApplication
@EnableNeo4jRepositories
@ComponentScan(
        basePackages = ["ch.hellorin.challengames.service", "ch.hellorin.challengames.rest"]
)
@Import(value = [
    ProviderConfiguration::class,
    CachingConfiguration::class,
    HttpConfiguration::class,
    SecurityConfiguration::class,
    MockedDataConfiguration::class,
    ChallengeStatusChangesConfiguration::class
])
class ChallengamesApplication

fun main(args: Array<String>) {
    runApplication<ChallengamesApplication>(*args)
}
