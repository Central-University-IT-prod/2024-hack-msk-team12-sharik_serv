package ru.isntrui.sharik.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.isntrui.sharik.models.Activity
import ru.isntrui.sharik.models.ActivityOwes
import ru.isntrui.sharik.repositories.ActivityOwesRepository
import ru.isntrui.sharik.repositories.ActivityRepository
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class ActivityService @Autowired constructor(
    private val activityRepo: ActivityRepository,
    private val activityOwesRepo: ActivityOwesRepository
) {
    fun create(activity: Activity): Activity = activityRepo.save(activity)

    fun createOwes(values: Iterable<ActivityOwes>): MutableList<ActivityOwes> = activityOwesRepo.saveAll(values)

    fun findById(id: UUID): Activity? = activityRepo.findById(id).getOrNull()

    fun findByRandanId(id: UUID) = activityRepo.findAllByRandanId(id)

    fun findOwesByActivityId(id: UUID) = activityOwesRepo.findAllByActivityId(id)
}
