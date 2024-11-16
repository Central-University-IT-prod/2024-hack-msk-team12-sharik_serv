package ru.isntrui.sharik.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.isntrui.sharik.models.Debt
import ru.isntrui.sharik.models.Randan
import ru.isntrui.sharik.models.User
import ru.isntrui.sharik.services.*
import java.util.*

@Tag(name = "Randan")
@RequestMapping("/api/randan")
@RestController
@Transactional
class RandanController @Autowired constructor(
    private val randanService: RandanService,
    private val debtService: DebtService,
    private val userService: UserService,
    private val transferService: TransferService,
    private val activityService: ActivityService
) {
    data class RandanResponseEntity(
        val id: UUID,
        val name: String,
        val isFinished: Boolean,
        val users: List<User>,
        val activities: List<ActivityResponseEntity>
    ) {
        companion object {
            @Transactional
            fun from(randan: Randan, service: ActivityService): RandanResponseEntity = RandanResponseEntity(
                id = randan.id,
                name = randan.name,
                isFinished = randan.isFinished,
                users = randan.users.toList(),
                activities = service.findByRandanId(randan.id).map {
                    ActivityResponseEntity(
                        id = it.id,
                        sum = it.sum,
                        name = it.name,
                        pays = it.pays,
                        owe = service.findOwesByActivityId(it.id).map {
                            OweResponseEntity(
                                who = it.owes,
                                amount = it.amount
                            )
                        }
                    )
                }
            )
        }
    }

    data class ActivityResponseEntity(
        val id: UUID,
        val sum: Int,
        val name: String,
        val pays: User,
        val owe: List<OweResponseEntity>
    )

    data class OweResponseEntity(val who: User, val amount: Int)

    @PostMapping
    @Operation(summary = "Create Randan")
    fun createRandan(@RequestBody randan: Randan): ResponseEntity<Randan> {
        try {
            randan.users.add(userService.getCurrentUser())
            val createdRandan = randanService.create(randan)
            return ResponseEntity.ok(createdRandan)
        } catch (_: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Randan by ID")
    fun getRandanById(@PathVariable @Parameter(description = "Randan ID") id: UUID): ResponseEntity<RandanResponseEntity> {
        val randan = randanService.findRandanById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(RandanResponseEntity.from(randan, activityService))
    }

    @GetMapping
    @Operation(summary = "Get randans of current user")
    fun getRandansOfCurrentUser(): ResponseEntity<List<RandanResponseEntity>> {
        val randans = randanService.findRandansByCurrentUser()
        return ResponseEntity.ok(randans.map { RandanResponseEntity.from(it, activityService) })
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Randan by ID")
    fun updateRandanById(
        @PathVariable @Parameter(description = "Randan ID") id: UUID, @RequestBody randan: Randan
    ): ResponseEntity<Randan> {
        val exRand = randanService.findRandanById(id)
        if (exRand == null) {
            return ResponseEntity.notFound().build()
        }
        if (id == randan.id) {
            val rand = randanService.create(randan)
            return ResponseEntity.ok(rand)
        } else {
            return ResponseEntity.badRequest().build()
        }
    }

    @PatchMapping("/{id}/addUser")
    @Operation(summary = "Add user to Randan")
    fun addUserToRandan(
        @PathVariable @Parameter(description = "Randan ID") id: UUID, @RequestBody username: String
    ): ResponseEntity<Randan> {
        val randan = randanService.findRandanById(id)
        if (randan == null) {
            return ResponseEntity.notFound().build()
        }
        val user = userService.findByUsername(username)
        if (user == null) {
            return ResponseEntity.notFound().build()
        }
        randan.users.add(user)
        val updatedRandan = randanService.create(randan)
        return ResponseEntity.ok(updatedRandan)
    }

    data class AllDebtsResponse(
        val youOweOthers: List<AllDebtsResponseEntry>, val othersOweYou: List<AllDebtsResponseEntry>
    )

    data class AllDebtsResponseEntry(val who: User, val amount: Int)

    @GetMapping("/{id}/debts")
    fun getAllDebts(@PathVariable id: UUID): ResponseEntity<AllDebtsResponse> {
        val userId = userService.getCurrentUser().id
        val debts = debtService.findByRandanId(id)
        val transfers = generateTransfers(debts)

        val response = AllDebtsResponse(youOweOthers = transfers.filter { it.from.id == userId }
            .map { AllDebtsResponseEntry(it.to, it.amount) },
            othersOweYou = transfers.filter { it.to.id == userId }.map { AllDebtsResponseEntry(it.from, it.amount) })
        return ResponseEntity.ok(response)
    }

    data class Transfer(val from: User, val to: User, val amount: Int)

    private fun generateTransfers(debts: Iterable<Debt>): List<Transfer> {
        val debtors = debts.filter { it.amount > 0 }.toMutableList()
        val creditors = debts.filter { it.amount < 0 }.onEach { it.amount = -it.amount }.toMutableList()

        debtors.sortByDescending { it.amount }
        creditors.sortByDescending { it.amount }

        val transfers = mutableListOf<Transfer>()

        while (debtors.isNotEmpty() && creditors.isNotEmpty()) {
            val debtor = debtors[0]
            val creditor = creditors[0]

            val transferAmount = minOf(debtor.amount, creditor.amount)

            transfers.add(Transfer(from = debtor.user, to = creditor.user, amount = transferAmount))

            debtors[0].amount = debtor.amount - transferAmount
            creditors[0].amount = creditor.amount - transferAmount

            if (debtors[0].amount == 0) debtors.removeFirst()

            if (creditors[0].amount == 0) creditors.removeFirst()
        }
        return transfers
    }

    data class TransferRequest(val from: UUID, val to: UUID, val amount: Int)

    @PostMapping("/{id}/transfer")
    @Operation(summary = "Transfer money from one user to another")
    fun transfer(@PathVariable id: UUID, @RequestBody request: TransferRequest): ResponseEntity<Void> {
        try {
            transferService.transfer(id, request.from, request.to, request.amount)
        } catch (_: NullPointerException) {
            return ResponseEntity.notFound().build()
        } catch (_: Exception) {
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok().build()
    }
}
