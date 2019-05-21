package ch.hellorin.challengames.configuration

import ch.hellorin.challengames.rest.FacadeMapper
import ch.hellorin.challengames.rest.challenge.ChallengesFacade
import ch.hellorin.challengames.rest.challenge.MyChallengesFacade
import ch.hellorin.challengames.rest.user.LoginFacade
import ch.hellorin.challengames.service.challenge.IChallengeService
import ch.hellorin.challengames.service.challenge.IMyChallengeService
import org.springframework.boot.test.context.TestComponent
import org.springframework.context.annotation.Bean

@TestComponent
class TestRestConfiguration {

    @Bean
    fun challengesFacade(challengeService: IChallengeService, mapper : FacadeMapper) : ChallengesFacade = ChallengesFacade(challengeService, mapper)

    @Bean
    fun myChallengesFacade(challengeService: IMyChallengeService, mapper: FacadeMapper) : MyChallengesFacade = MyChallengesFacade(challengeService, mapper)

    @Bean
    fun loginFacade() : LoginFacade = LoginFacade()
}