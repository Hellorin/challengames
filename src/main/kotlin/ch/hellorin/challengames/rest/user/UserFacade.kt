package ch.hellorin.challengames.rest.user

import ch.hellorin.challengames.PlayerDto
import ch.hellorin.challengames.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.ws.rs.Produces

@RestController
@RequestMapping("/user")
@Produces("application/json")
class UserFacade(val userService: UserService) {

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: Long) : ResponseEntity<PlayerDto> {
        return ResponseEntity.ok(PlayerDto(id, userService.getUserById(id).name))
    }

    @GetMapping("/")
    fun getAllUsers() : ResponseEntity<List<PlayerDto>> = ResponseEntity.ok(userService.getAllUsers().map { PlayerDto(it.id!!, it.name) }.toList())
}