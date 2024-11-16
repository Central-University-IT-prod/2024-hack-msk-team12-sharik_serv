package ru.isntrui.sharik.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.isntrui.sharik.models.Debt
import ru.isntrui.sharik.services.DebtService
import java.util.UUID

@RestController
@Tag(name = "Debt")
@RequestMapping("/api/debt")
class DebtController @Autowired constructor(
    private val debtService: DebtService,
) {
    @GetMapping("{id}")
    @Operation(summary = "Get info about debt by id")
    fun get(@PathVariable @Parameter(description = "debt id") id: UUID): ResponseEntity<Debt> {
        val debt = debtService.findById(id)
        return if (debt.isPresent) {
            ResponseEntity.ok(debt.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    @Operation(summary = "Save debt")
    fun save(@Parameter(description = "debt") @RequestBody debt: Debt): ResponseEntity<Debt> {
        return ResponseEntity.ok(debtService.save(debt))
    }

    @PutMapping("{id}")
    @Operation(summary = "Update debt by id")
    fun update(
        @PathVariable @Parameter(description = "debt id") id: UUID, @RequestBody debt: Debt
    ): ResponseEntity<Debt> {
        val updatedDebt = debtService.save(debt)
        return ResponseEntity.ok(updatedDebt)
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete debt by id")
    fun delete(@PathVariable @Parameter(description = "debt id") id: UUID): ResponseEntity<Void> {
        debtService.deleteById(id)
        return ResponseEntity.ok().build()
    }
}

