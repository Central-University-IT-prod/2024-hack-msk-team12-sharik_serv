package ru.isntrui.sharik.models

import jakarta.persistence.*

@Entity
@Table(name = "activity_owes")
@IdClass(ActivityOwesId::class)
data class ActivityOwes(
    @Id @ManyToOne var activity: Activity,

    @Id @ManyToOne var owes: User,

    var amount: Int
)
