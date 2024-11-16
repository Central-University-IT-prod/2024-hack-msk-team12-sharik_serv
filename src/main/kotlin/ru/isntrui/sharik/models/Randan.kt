package ru.isntrui.sharik.models

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "randans")
// @EqualsAndHashCode(exclude = ["activities", "debts", "users"])
data class Randan(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID of the Randan", example = "1")
    var id: UUID = UUID.randomUUID(),

    @Schema(description = "Name of the Randan", example = "Sasha's birthday!")
    var name: String = "",

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "randan")
    @Schema(description = "Activities that are part of the Randan")
    var activities: MutableSet<Activity> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "randan")
    var debts: MutableSet<Debt> = mutableSetOf(),

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "randan_user",
        joinColumns = [JoinColumn(name = "randan_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    @Schema(description = "Users who are part of the Randan")
    var users: MutableSet<User> = mutableSetOf(),

    @Schema(description = "Is randan finished", example = false.toString())
    var isFinished: Boolean = false
) {
    constructor() : this(UUID.randomUUID(), "", mutableSetOf(), mutableSetOf(), mutableSetOf(), false)
}
