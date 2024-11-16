package ru.isntrui.sharik.services

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.isntrui.sharik.models.Randan
import ru.isntrui.sharik.repositories.RandanRepository
import java.util.*

@Service
class RandanService @Autowired constructor(val randanRepository: RandanRepository, val userService: UserService) {
    @Transactional
    fun findRandanByName(name: String) = randanRepository.findByName(name)

    @Transactional
    fun findRandanById(id: UUID): Randan? {
        val out = randanRepository.findById(id).get()
        return out
    }

    @Transactional
    fun create(randan: Randan) = randanRepository.save(randan)

    @Transactional
    fun findRandansByCurrentUser(): List<Randan> {
        val out = randanRepository.findAllByUsersId(userService.getCurrentUser().id)
        for (it in out) {
            println(it.users)
        }
        return out
    }
}
