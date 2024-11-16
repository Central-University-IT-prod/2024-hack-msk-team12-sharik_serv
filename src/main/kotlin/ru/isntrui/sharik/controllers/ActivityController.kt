package ru.isntrui.sharik.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.isntrui.sharik.models.Activity
import ru.isntrui.sharik.models.ActivityOwes
import ru.isntrui.sharik.models.Debt
import ru.isntrui.sharik.models.User
import ru.isntrui.sharik.services.ActivityService
import ru.isntrui.sharik.services.DebtService
import ru.isntrui.sharik.services.RandanService
import ru.isntrui.sharik.services.UserService
import java.util.*

@RequestMapping("/api/activity")
@RestController
class ActivityController @Autowired constructor(
    private val activityService: ActivityService,
    private val debtService: DebtService,
    private val randanService: RandanService,
    private val userService: UserService
) {
    data class NewActivityRequestBody(
        val name: String,
        val sum: Int,
        val randanId: UUID,
        val debts: Set<NewActivityRequestBodyEntry>
    )

    data class NewActivityRequestBodyEntry(val username: String, val amount: Int)

    data class GetActivityResponseBody(val pays: User, val entries: List<GetActivityResponseBodyEntry>)
    data class GetActivityResponseBodyEntry(val user: User, val amount: Int)

    @Operation(summary = "Create activity")
    @PostMapping
    fun create(@RequestBody @Parameter body: NewActivityRequestBody): ResponseEntity<Void> {
        val randan = randanService.findRandanById(body.randanId) ?: return ResponseEntity.notFound().build()
        val activity = activityService.create(
            Activity(
                name = body.name,
                sum = body.sum,
                randan = randan,
                pays = userService.getCurrentUser()
            )
        )
        val activityOwes = body.debts.map {
            ActivityOwes(
                activity = activity,
                owes = userService.findByUsername(it.username) ?: return ResponseEntity.notFound().build(),
                amount = it.amount
            )
        }
        activityService.createOwes(activityOwes)
        val debts = debtService.findByRandanId(body.randanId)
        var acc = 0
        for (d in body.debts) {
            val debt = debts.find { it.user.username == d.username }
            if (debt == null) {
                debts.add(
                    Debt(
                        user = userService.findByUsername(d.username) ?: return ResponseEntity.notFound().build(),
                        randan = randan,
                        amount = d.amount
                    )
                )
            } else {
                debt.amount += d.amount
                acc += d.amount
            }
        }

        debtService.saveAll(debts)
        debtService.save(
            Debt(
                user = userService.getCurrentUser(),
                randan = randan,
                amount = -acc
            )
        )
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "Get activity by id")
    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ResponseEntity<GetActivityResponseBody> {
        val activity = activityService.findById(id) ?: return ResponseEntity.notFound().build()
        val userId = userService.getCurrentUser().id
        if (activity.pays.id != userId && activity.activityOwes.all { it.owes.id != userId }) {
            return ResponseEntity.status(403).build()
        }

        return ResponseEntity.ok(
            GetActivityResponseBody(
                pays = activity.pays,
                entries = activity.activityOwes.map {
                    GetActivityResponseBodyEntry(it.owes, it.amount)
                }
            )
        )
    }

    @Operation(summary = "Get all activities by randan")
    @GetMapping("/allByRandan")
    fun getAllByRandan(@RequestParam randanId: UUID): ResponseEntity<Set<Activity>> {
        val randan = randanService.findRandanById(randanId) ?: return ResponseEntity.notFound().build()
        val userId = userService.getCurrentUser().id
        if (randan.users.all { it.id != userId }) {
            return ResponseEntity.status(403).build()
        }
        return ResponseEntity.ok(randan.activities)
    }
}
