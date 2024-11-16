package ru.isntrui.sharik.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.isntrui.sharik.models.Debt
import java.util.*

interface DebtRepository : JpaRepository<Debt, UUID> {
    fun findByRandanId(id: UUID): MutableSet<Debt>
    fun findByRandanIdAndUserId(id: UUID, userId: UUID): Debt?
}
