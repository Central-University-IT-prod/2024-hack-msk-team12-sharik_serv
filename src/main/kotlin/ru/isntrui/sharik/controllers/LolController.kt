package ru.isntrui.sharik.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/lol")
class LolController {
    @GetMapping
    fun lol(): ResponseEntity<ResponseEntity<Nothing>> {
        return ResponseEntity.ok(ResponseEntity.internalServerError().build())
    }
}