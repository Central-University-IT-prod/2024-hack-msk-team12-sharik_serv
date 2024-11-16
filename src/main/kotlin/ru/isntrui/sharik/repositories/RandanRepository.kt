package ru.isntrui.sharik.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.isntrui.sharik.models.Randan
import java.util.*

interface RandanRepository : JpaRepository<Randan, UUID> {
    override fun findById(id: UUID): Optional<Randan?>

    fun findByName(name: String): Randan?

    fun findAllByUsersId(userId: UUID): List<Randan>
}
