package ru.isntrui.sharik.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.isntrui.sharik.models.Debt
import ru.isntrui.sharik.repositories.DebtRepository
import java.util.*

@Service
class DebtService @Autowired constructor(private val debtRepo: DebtRepository) {
    fun saveAll(debts: Iterable<Debt>): List<Debt> = debtRepo.saveAll(debts)

    fun findByRandanId(id: UUID) = debtRepo.findByRandanId(id)

    fun findById(id: UUID) = debtRepo.findById(id)

    fun save(debt: Debt) = debtRepo.save(debt)

    fun deleteById(id: UUID) = debtRepo.deleteById(id)
}
