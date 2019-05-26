package ch.hellorin.challengames.rest.user

import ch.hellorin.challengames.PlayerDto
import ch.hellorin.challengames.rest.FacadeMapper
import ch.hellorin.challengames.service.user.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.ws.rs.Produces

@RestController
@RequestMapping("/me/user")
@Produces("application/json")
class MyUserFacade(val userService: UserService, val mapper: FacadeMapper) {

    @GetMapping
    fun myUser(principal: Principal?) : PlayerDto {
        return mapper.mapBusinessToDto(
            userService.myUserInfo(principal!!)
        )
    }
}