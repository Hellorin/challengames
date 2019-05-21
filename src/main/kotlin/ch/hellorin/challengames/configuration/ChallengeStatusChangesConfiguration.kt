package ch.hellorin.challengames.configuration

import org.kie.api.KieServices
import org.kie.api.runtime.KieContainer
import org.kie.internal.io.ResourceFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Component

@Component
@ComponentScan(basePackages = ["ch.hellorin.challengames.service.challenge.status"])
class ChallengeStatusChangesConfiguration {
    private val drlFile: String = "CHALLENGES_STATUS_RULE.drl"

    @Bean
    fun kieContainer() : KieContainer {
        val factory = KieServices.Factory.get()

        val newKieFileSystem = factory.newKieFileSystem()
        newKieFileSystem.write(ResourceFactory.newClassPathResource(drlFile))
        val newKieBuilder = factory.newKieBuilder(newKieFileSystem)
        newKieBuilder.buildAll()

        val kieModule = newKieBuilder.kieModule
        return factory.newKieContainer(kieModule.releaseId)
    }
}