package ch.hellorin.challengames.rest.user

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.ws.rs.Produces

@RestController
@RequestMapping("/login")
@Produces("application/json")
class LoginFacade {
    private val connectedUsers = mutableMapOf<String, Boolean>()

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun login(principal : Principal?) : String {
        connectedUsers[principal!!.name] = true

        return "OK"
    }

    @GetMapping("/{name}")
    fun isLogged(@PathVariable("name") name: String) : Boolean = connectedUsers[name] ?: false

}