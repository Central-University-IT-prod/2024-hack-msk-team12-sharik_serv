package ru.isntrui.sharik.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.isntrui.sharik.models.ActivityOwes
import java.util.*

interface ActivityOwesRepository : JpaRepository<ActivityOwes, UUID> {
    fun findAllByActivityId(id: UUID): List<ActivityOwes>
}
