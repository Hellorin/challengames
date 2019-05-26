package ch.hellorin.challengames.configuration

import ch.hellorin.challengames.service.user.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.dao.DaoAuthenticationProvider


@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var userDetailsService: UserService

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.authenticationProvider(authenticationProvider())
    }

    @Bean
    fun authenticationProvider() : DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(getEncoder())
        return authProvider
    }

    override fun configure(http: HttpSecurity?) {
        http!!.authorizeRequests()

                .antMatchers(HttpMethod.GET, "/challenges").authenticated()
                .antMatchers(HttpMethod.GET, "/challenges/*").authenticated()
                .antMatchers(HttpMethod.PUT, "/challenges/").authenticated()
                .antMatchers(HttpMethod.GET, "/login/*").authenticated()
                .antMatchers(HttpMethod.GET, "/user/**").authenticated()

                .antMatchers(HttpMethod.GET, "/me/**").authenticated()

                .and().httpBasic()

                .and().csrf().disable()
    }

    @Bean("passwordEncoder")
    fun getEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}