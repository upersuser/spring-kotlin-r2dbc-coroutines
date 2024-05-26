package dev.upersuser.companycrud.controller

import dev.upersuser.companycrud.dto.UserRequest
import dev.upersuser.companycrud.dto.UserResponse
import dev.upersuser.companycrud.service.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    suspend fun createUser(
        @RequestBody request: UserRequest
    ): UserResponse =
        userService.saveUser(request.toUser())
            ?.let { UserResponse.fromUser(it) }
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)

    @GetMapping
    suspend fun getUsers(
        @RequestParam("name", required = false) name: String?
    ): Flow<UserResponse> =
        (name?.let { userService.findByNameLike(it) } ?: userService.findAllUsers())
            .mapNotNull { UserResponse.fromUser(it) }

    @GetMapping("/{id}")
    suspend fun getUserById(
        @PathVariable("id") id: Long
    ): UserResponse =
        userService.findById(id)?.let { UserResponse.fromUser(it) }
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PutMapping("/{id}")
    suspend fun updateById(
        @PathVariable("id") id: Long,
        @RequestBody request: UserRequest
    ): UserResponse =
        UserResponse.fromUser(userService.updateById(id, request.toUser()))
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)

    @DeleteMapping("/{id}")
    suspend fun deleteUserById(
        @PathVariable("id") id: Long
    ) { userService.deleteById(id) }
}