package ru.isntrui.sharik.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "debts")
// @EqualsAndHashCode(exclude = ["user", "randan"])
data class Debt(
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID = UUID.randomUUID(),

    @ManyToOne var user: User,

    @ManyToOne @JsonIgnore var randan: Randan,

    var amount: Int
)
