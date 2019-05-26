package ch.hellorin.challengames.service.user

import ch.hellorin.challengames.exception.ExistingUserException
import ch.hellorin.challengames.exception.MissingUserException
import ch.hellorin.challengames.persistance.model.node.User
import ch.hellorin.challengames.persistance.repository.RoleRepository
import ch.hellorin.challengames.persistance.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal
import kotlin.streams.toList

@Service
@Transactional
class UserService(
        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository) : IUserService, IMyUserService {

    private val LOGGER : Logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    private var passwordEncoder: PasswordEncoder? = null

    override fun loadUserByUsername(username: String?): UserDetails {
        var user = userRepository.findByName(username!!)

        if (user != null) {
            val listRoles = user.roles.map { SimpleGrantedAuthority(it.name) }.toList()
            return org.springframework.security.core.userdetails.User(user.name, user.credential, true, true, true, true, listRoles)
        } else {
            throw MissingUserException()
        }
    }

    @Throws(ExistingUserException::class)
    override fun addUser(username: String, email:String?, password: String, roles : List<String>) : Long {
        if (userRepository.findByName(username) == null) {
            LOGGER.info("The user doesn't already exist -> creating it")
            var user = User(
                    username,
                    email,
                    passwordEncoder!!.encode(password),
                    roles.stream().map { roleRepository.findByName(it)!! }.toList()
            )
            return userRepository.save(user).id!!
        } else {
            LOGGER.info("The user {} that we are trying to create already exist !", username)
            throw ExistingUserException()
        }
    }

    @Throws(MissingUserException::class)
    override fun getUser(username: String) : User = userRepository.findByName(username) ?: throw MissingUserException()

    @Throws(MissingUserException::class)
    override fun getUserById(id: Long) : User = userRepository.findById(id).orElse(null) ?: throw MissingUserException()

    override fun getAllUsers() : Iterable<User> {
        return userRepository.findAll()
    }

    @Throws(MissingUserException::class)
    override fun myUserInfo(principal : Principal) : User = userRepository.findByName(principal.name) ?: throw MissingUserException()
}

interface IUserService : UserDetailsService {
    fun addUser(username: String, email:String?, password: String, roles : List<String>) : Long

    fun getUser(username: String) : User

    fun getAllUsers() : Iterable<User>

    fun getUserById(id: Long) : User
}

interface IMyUserService {
    fun myUserInfo(principal : Principal) : User
}