package ru.isntrui.sharik.models

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "transfers")
// @EqualsAndHashCode(exclude = ["from", "to"])
data class Transfer(
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID,

    @ManyToOne var from: User,

    @ManyToOne var to: User,

    var amount: Int
)
