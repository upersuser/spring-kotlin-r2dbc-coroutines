package dev.upersuser.companycrud.controller

import dev.upersuser.companycrud.dto.UserRequest
import dev.upersuser.companycrud.dto.UserResponse
import dev.upersuser.companycrud.model.User
import dev.upersuser.companycrud.service.UserService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UserControllerUnitTest {

    private val mockUserService: UserService = mock()

    private val mockUserController: UserController = UserController(mockUserService)

    @Test
    fun createUser() {
        val request = UserRequest("name", "email", 5, 1)

        runBlocking {
            val userModel = request.toUser()

            whenever(mockUserService.saveUser(userModel)).thenReturn(userModel.copy(1))

            val expected = UserResponse.fromUser(userModel.copy(1))

            val actual = mockUserController.createUser(request)

            assertEquals(expected, actual)
        }
    }

    @Test
    fun createUserFail() {
        val request = UserRequest("name", "email", 5, 1)

        runBlocking {
            val userModel = request.toUser()

            whenever(mockUserService.saveUser(userModel)).thenReturn(userModel)

            val expected = HttpStatus.INTERNAL_SERVER_ERROR

            val actual = assertThrows<ResponseStatusException> {
                mockUserController.createUser(request)
            }

            assertEquals(expected, actual.statusCode)
        }
    }

    @Test
    fun getUserById() {
        val userId = 1L

        val user = User(userId, "email", "name", 12, 1)

        runBlocking {
            whenever(mockUserService.findById(userId)).thenReturn(user)

            val expected = UserResponse.fromUser(user)

            val actual = mockUserController.getUserById(userId)

            assertEquals(expected, actual)
        }
    }

    @Test
    fun getUserByIdFail() {
        val userId = 1L

        runBlocking {
            whenever(mockUserService.findById(userId)).thenReturn(null)

            val expected = HttpStatus.NOT_FOUND

            val actual = assertThrows<ResponseStatusException> {
                mockUserController.getUserById(userId)
            }

            assertEquals(expected, actual.statusCode)
        }
    }

    @Test
    fun getUsers() {
        val users = flowOf(
            User(1, "email", "name", 12, 8),
            User(2, "email", "name", 12, 4)
        )

        runBlocking {
            whenever(mockUserService.findAllUsers()).thenReturn(users)

            val expected = users.mapNotNull { UserResponse.fromUser(it) }.toList()

            val actual = mockUserController.getUsers(null).toList()

            assertEquals(expected, actual)
        }
    }

    @Test
    fun updateById() {
    }

    @Test
    fun deleteUserById() {

    }
}