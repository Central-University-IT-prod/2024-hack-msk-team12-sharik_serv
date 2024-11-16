package ru.isntrui.sharik

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<SharikApplication>().with(TestcontainersConfiguration::class).run(*args)
}
