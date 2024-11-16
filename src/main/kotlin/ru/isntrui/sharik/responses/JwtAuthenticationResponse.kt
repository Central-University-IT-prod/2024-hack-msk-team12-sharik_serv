package ru.isntrui.sharik.responses

import io.swagger.v3.oas.annotations.media.Schema
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Schema(description = "Ответ c токеном доступа")
data class JwtAuthenticationResponse(
    @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdZsaAiJhZGq3ASsmV4cCI6MTYyMjUwNj...")
    val token: String? = null
)