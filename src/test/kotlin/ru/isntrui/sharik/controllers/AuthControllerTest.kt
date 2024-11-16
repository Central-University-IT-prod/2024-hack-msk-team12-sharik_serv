package ru.isntrui.sharik.controllers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import ru.isntrui.sharik.requests.SignUpRequest
import ru.isntrui.sharik.services.AuthenticationService
import kotlin.test.Test

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import ru.isntrui.sharik.requests.SignInRequest

@ExtendWith(MockitoExtension::class)
class AuthControllerTest {
    @Mock
    private lateinit var authService: AuthenticationService

    @InjectMocks
    private lateinit var authController: AuthController

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build()
    }
    val username: String = System.currentTimeMillis().toString()

    @Test
    fun `should return 200 and 400 status code when sign up`() {
        val signUpRequest = SignUpRequest()
        signUpRequest.username = username
        signUpRequest.password = "{{sensitive_data}}"
        mockMvc.perform(post("/api/auth/sign-up")
            .contentType("application/json")
            .content(ObjectMapper().writeValueAsString(signUpRequest)))
            .andExpect(status().isOk)
    }

    @Test
    fun `should return 200 status code when sign in`() {
        val signInRequest = SignInRequest()
        signInRequest.username = username
        signInRequest.password = "{{sensitive_data}}"
        mockMvc.perform(post("/api/auth/sign-in")
            .contentType("application/json")
            .content(ObjectMapper().writeValueAsString(signInRequest)))
            .andExpect(status().isOk)
    }
}