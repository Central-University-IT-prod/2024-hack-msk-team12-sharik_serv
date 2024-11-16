package ru.isntrui.sharik.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.isntrui.sharik.models.Activity
import java.util.*

interface ActivityRepository : JpaRepository<Activity, UUID> {
    fun findAllByRandanId(id: UUID): List<Activity>
}
