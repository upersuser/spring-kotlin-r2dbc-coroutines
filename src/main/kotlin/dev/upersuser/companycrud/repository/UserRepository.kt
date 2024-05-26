package dev.upersuser.companycrud.repository

import dev.upersuser.companycrud.model.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CoroutineCrudRepository<User, Long> {

    fun findByNameContaining(name: String): Flow<User>

    fun findByCompanyId(companyId: Long): Flow<User>

    @Query("select * from application.app_user where email = :email")
    fun findByEmail(email: String): Flow<User>
}