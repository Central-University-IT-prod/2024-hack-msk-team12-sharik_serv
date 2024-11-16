package ru.isntrui.sharik.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.isntrui.sharik.requests.SignInRequest
import ru.isntrui.sharik.requests.SignUpRequest
import ru.isntrui.sharik.responses.JwtAuthenticationResponse
import ru.isntrui.sharik.services.AuthenticationService

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
class AuthController @Autowired constructor(
    private val authenticationService: AuthenticationService
) {

    @Operation(summary = "Register a new user")
    @PostMapping("/sign-up")
    fun signUp(@RequestBody @Valid request: SignUpRequest): ResponseEntity<JwtAuthenticationResponse?> {
        return try {
            ResponseEntity.ok(authenticationService.signUp(request))
        } catch (_: RuntimeException) {
            ResponseEntity.badRequest().build()
        } catch (_: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @Operation(summary = "Authenticate a user")
    @PostMapping("/sign-in")
    fun signIn(@RequestBody @Valid request: SignInRequest): ResponseEntity<JwtAuthenticationResponse?> {
        return try {
            ResponseEntity.ok(authenticationService.signIn(request))
        } catch (_: RuntimeException) {
            ResponseEntity.badRequest().build()
        } catch (_: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }
}