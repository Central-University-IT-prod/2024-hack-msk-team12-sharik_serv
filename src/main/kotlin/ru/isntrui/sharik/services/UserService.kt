package ru.isntrui.sharik.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.isntrui.sharik.models.User
import ru.isntrui.sharik.models.UserResponse
import ru.isntrui.sharik.repositories.UserRepository
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class UserService @Autowired constructor(val repository: UserRepository) : UserDetailsService {
    fun save(user: User): User {
        return repository.save(user)
    }

    fun findById(id: UUID): User? = repository.findById(id).getOrNull()

    fun create(user: User): User {
        if (repository.existsByUsername(user.username)) {
            throw RuntimeException("Пользователь с таким именем уже существует")
        }

        return save(user)
    }

    fun findByUsername(username: String): User? = repository.findByUsername(username).getOrNull()

    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            findByUsername(username) ?: throw UsernameNotFoundException("User not found with username: $username")
        }
    }

    fun getCurrentUser(): User {
        val username = SecurityContextHolder.getContext().authentication.name
        return findByUsername(username) ?: throw RuntimeException("Не авторизован")
    }

    override fun loadUserByUsername(username: String): UserDetails =
        findByUsername(username) ?: throw UsernameNotFoundException("User not found with username: $username")

    fun delete(user: User) {
        repository.delete(user)
    }

    fun userToUserResponse(user: User): UserResponse {
        return UserResponse(user.id, user.username, user.firstName, user.lastName, user.avatarURL)
    }

    fun userResponseToUser(userResponse: UserResponse) : User {
        return User(userResponse.id, userResponse.username, "", userResponse.firstName, userResponse.lastName, userResponse.avatarURL)
    }
}