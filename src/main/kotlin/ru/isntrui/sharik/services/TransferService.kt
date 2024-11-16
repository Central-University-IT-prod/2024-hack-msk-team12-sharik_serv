package ru.isntrui.sharik.services

import jakarta.transaction.Transactional
import jakarta.validation.constraints.Null
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.isntrui.sharik.models.Transfer
import ru.isntrui.sharik.repositories.DebtRepository
import ru.isntrui.sharik.repositories.RandanRepository
import ru.isntrui.sharik.repositories.TransferRepository
import java.lang.NullPointerException
import java.util.UUID
import kotlin.jvm.Throws
import kotlin.jvm.optionals.getOrNull

@Service
class TransferService @Autowired constructor(
    private val transferRepository: TransferRepository,
    private val debtRepository: DebtRepository,
    private val userService: UserService
) {
    @Throws(NullPointerException::class)
    @Transactional
    fun transfer(fromId: UUID, toId: UUID, randanId: UUID, amount: Int) {
        val from = userService.findById(fromId)!!
        val to = userService.findById(toId)!!

        transferRepository.save(Transfer(UUID.randomUUID(),
            from, to, amount
        ))

        val fromDebt = debtRepository.findByRandanIdAndUserId(randanId, fromId)!!
        val toDebt = debtRepository.findByRandanIdAndUserId(randanId, toId)!!

        fromDebt.amount -= amount
        toDebt.amount += amount

        debtRepository.save(fromDebt)
        debtRepository.save(toDebt)
    }
}