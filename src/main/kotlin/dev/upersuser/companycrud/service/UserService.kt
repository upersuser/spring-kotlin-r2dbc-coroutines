package dev.upersuser.companycrud.service

import dev.upersuser.companycrud.model.User
import dev.upersuser.companycrud.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository
) {

    suspend fun saveUser(user: User): User? =
        userRepository.findByEmail(user.email).firstOrNull()
            ?.let { throw ResponseStatusException(HttpStatus.BAD_REQUEST) }
            ?: userRepository.save(user)

    suspend fun findAllUsers(): Flow<User> =
        userRepository.findAll()

    suspend fun findById(id: Long): User? =
        userRepository.findById(id)

    suspend fun deleteById(id: Long) {
        userRepository.findById(id)?.also {
            userRepository.deleteById(id)
        } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    suspend fun updateById(id: Long, requestedUser: User): User =
        userRepository.findById(id)?.let {
            userRepository.save(
                requestedUser.copy(id = it.id)
            )
        } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    suspend fun findByCompanyId(id: Long): Flow<User> =
        userRepository.findByCompanyId(id)

    suspend fun findByNameLike(name: String): Flow<User> =
        userRepository.findByNameContaining(name)

}