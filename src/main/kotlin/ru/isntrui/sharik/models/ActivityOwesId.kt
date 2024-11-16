package ru.isntrui.sharik.models

import java.io.Serializable
import java.util.*

data class ActivityOwesId(var activity: UUID = UUID.randomUUID(), var owes: UUID = UUID.randomUUID()) : Serializable
