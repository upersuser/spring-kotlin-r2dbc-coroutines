package dev.upersuser.companycrud.repository

import dev.upersuser.companycrud.model.Company
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository: CoroutineCrudRepository<Company, Long> {

    fun findByNameContaining(name: String): Flow<Company>
}