package ru.isntrui.sharik.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.isntrui.sharik.models.Transfer
import java.util.UUID

interface TransferRepository : JpaRepository<Transfer, UUID> {}