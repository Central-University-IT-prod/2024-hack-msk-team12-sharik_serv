package ru.isntrui.sharik.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.isntrui.sharik.models.User
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    fun findByUsername(userName: String): Optional<User>
    fun existsByUsername(userName: String): Boolean
}
