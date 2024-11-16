package ru.isntrui.sharik.services

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.isntrui.sharik.models.User
import ru.isntrui.sharik.requests.SignInRequest
import ru.isntrui.sharik.requests.SignUpRequest
import ru.isntrui.sharik.responses.JwtAuthenticationResponse

@Service
class AuthenticationService(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager
) {

    fun signUp(request: SignUpRequest): JwtAuthenticationResponse {
        val user = User(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            firstName = request.firstName,
            lastName = request.lastName,
            avatarURL = "http://igw.isntrui.ru:1401/res/prod_avatar_1962280672_5534-2197.jpg"
        )

        userService.create(user)

        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(token = jwt)
    }

    fun signIn(request: SignInRequest): JwtAuthenticationResponse {
        authenticationManager.authenticate(
            if (userService.findByUsername(request.username!!) == null) {
                throw RuntimeException("User not found")
            } else
            UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        )

        val user = userService
            .userDetailsService()
            .loadUserByUsername(request.username!!) as User

        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(token = jwt)
    }
}
