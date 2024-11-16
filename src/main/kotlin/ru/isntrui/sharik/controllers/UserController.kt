package ru.isntrui.sharik.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.isntrui.sharik.models.User
import ru.isntrui.sharik.models.UserResponse
import ru.isntrui.sharik.services.UserService
import java.util.*

@RestController
@Tag(name = "User")
@RequestMapping("/api/user")
class UserController @Autowired constructor(private val userService: UserService) {
    @GetMapping("{id}")
    @Operation(summary = "Get user by id")
    fun getUserById(@PathVariable @Parameter(description = "User id") id: UUID): ResponseEntity<UserResponse> {
        try {
            return ResponseEntity.ok(userService.userToUserResponse(userService.findById(id)!!))
        } catch (e: RuntimeException) {
            println(e)
            return ResponseEntity.notFound().build()
        } catch (e: Exception) {
            println(e)
            return ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping()
    @Operation(summary = "Get current user")
    fun getCurrentUser(): ResponseEntity<UserResponse> {
        try {
            return ResponseEntity.ok(userService.userToUserResponse(userService.getCurrentUser()))
        } catch (e: RuntimeException) {
            println(e)
            return ResponseEntity.status(403).build()
        } catch (e: Exception) {
            println(e)
            return ResponseEntity.internalServerError().build()
        }
    }

    @PutMapping()
    @Operation(summary = "Update current user")
    fun updateUserById(@RequestBody user: User): ResponseEntity<UserResponse> {
        val id = userService.getCurrentUser().id
        try {
            val updatedUser = userService.findById(id)!!.copy(
                username = user.username,
                avatarURL = user.avatarURL,
                firstName = user.firstName,
                lastName = user.lastName,
            )
            return ResponseEntity.ok(userService.userToUserResponse(userService.save(updatedUser)))
        } catch (e: NullPointerException) {
            println(e)
            return ResponseEntity.notFound().build()
        } catch (e: Exception) {
            println(e)
            return ResponseEntity.internalServerError().build()
        }
    }

    @DeleteMapping()
    @Operation(summary = "Delete current user")
    fun deleteUserById(): ResponseEntity<Void> {
        val user = userService.getCurrentUser()
        try {
            userService.delete(user)
            return ResponseEntity.ok().build()
        } catch (e: RuntimeException) {
            println(e)
            return ResponseEntity.notFound().build()
        } catch (e: Exception) {
            println(e)
            return ResponseEntity.internalServerError().build()
        }
    }
}
