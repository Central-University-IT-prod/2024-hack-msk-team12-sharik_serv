package ru.isntrui.sharik.requests

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import lombok.Data

@Schema(description = "Sign-up request")
class SignUpRequest {
    @Schema(description = "Username", example = "isntrui")
    var username: @Size(
        min = 5,
        max = 50,
        message = "Имя пользователя должно содержать от 5 до 50 символов"
    ) @NotBlank(message = "Имя пользователя не может быть пустыми") String = ""

    @Schema(description = "Password", example = "mypassword")
    var password: @Size(
        min = 8,
        max = 255,
        message = "Длина пароля должна быть от 8 до 255 символов"
    ) @NotBlank(message = "Пароль не может быть пустыми") String = ""

    @Schema(description = "First name", example = "Sasha")
    var firstName: @NotBlank(message = "Имя не может быть пустым") String = ""

    @Schema(description = "Last name", example = "Bely")
    var lastName: @NotBlank(message = "Фамилия не может быть пустой") String = ""
}