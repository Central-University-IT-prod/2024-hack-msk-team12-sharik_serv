package ru.isntrui.sharik.models

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "activities")
// @EqualsAndHashCode(exclude = ["pays", "randan", "activityOwes"])
data class Activity(
    @Id @GeneratedValue(strategy = GenerationType.UUID) @Schema(
        description = "ID of the Activity", example = "3"
    ) var id: UUID = UUID.randomUUID(),
    @Schema(description = "Name of the Activity", example = "Restaurant") var name: String = "",

    var sum: Int = 0,

    @ManyToOne var pays: User,

    @ManyToOne
    @JsonIgnore
    var randan: Randan,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "activity")
    @JsonIgnore var activityOwes: MutableSet<ActivityOwes> = mutableSetOf()
) {
    constructor() : this(UUID.randomUUID(), "", 0, User(), Randan(), mutableSetOf())

    data class Owes(val user: User, val amount: Int)

    val owes
        get() = activityOwes.map { Owes(it.owes, it.amount) }
}
