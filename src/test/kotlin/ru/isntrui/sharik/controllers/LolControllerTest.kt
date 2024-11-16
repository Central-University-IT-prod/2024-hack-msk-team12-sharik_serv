package ru.isntrui.sharik.controllers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class LolControllerTest {
    @InjectMocks
    private lateinit var lolController: LolController

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lolController).build()
    }

    val username: String = System.currentTimeMillis().toString()

    @Test
    fun `should return 200 status code when lol`() {
        mockMvc.perform(
            get("/lol")
                .contentType("application/json")
        )
            .andExpect(status().isOk)
    }
}
