package ru.isntrui.sharik

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SharikApplication

fun main(args: Array<String>) {
    runApplication<SharikApplication>(*args)
}
