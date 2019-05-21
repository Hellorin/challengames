package ch.hellorin.challengames.service.provider

import ch.hellorin.challengames.exception.MissingGameException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory

interface IGameProvider {
    fun tryToGetGame(name: String, id: Long? = null): GameDataDto
}

@Profile("!localtest")
class GameProvider (
        private val httpRequestFactory: HttpComponentsClientHttpRequestFactory,
        @Qualifier("igdb-http")
        val httpHeaders: HttpHeaders,
        @Qualifier("oneResult")
        val strategy: GameSelectionStrategy) : IGameProvider {

    @Value("\${igdb.uri}")
    val url: String? = null

    @Throws(CannotChooseGameException::class, MissingGameException::class)
    override fun tryToGetGame(name: String, id: Long?): GameDataDto {
        var objects: Array<GameDataDto>?
        if (id != null) {
            objects = RestTemplate(httpRequestFactory).postForObject(
                    url!!,
                    HttpEntity("${QueryHelper.buildFieldsQuery(GameDataDto::class)} where id = $id;", httpHeaders),
                    Array<GameDataDto>::class.java)
            if (objects != null) {
                return objects.first()
            } else {
                throw MissingGameException()
            }
        } else {
            objects = RestTemplate(httpRequestFactory).postForObject(
                    url!!,
                    HttpEntity("search \"$name\";${QueryHelper.buildFieldsQuery(GameDataDto::class)};", httpHeaders),
                    Array<GameDataDto>::class.java)
            return strategy.select(objects!!)
        }

    }
}

data class GameDataDto(val id: Long, var name: String, val slug: String, val summary: String)
