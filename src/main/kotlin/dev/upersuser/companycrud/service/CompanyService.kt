package dev.upersuser.companycrud.service

import dev.upersuser.companycrud.model.Company
import dev.upersuser.companycrud.repository.CompanyRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CompanyService(
    private val companyRepository: CompanyRepository
) {
    suspend fun findById(id: Long): Company? =
        companyRepository.findById(id)

    suspend fun findAllCompanies(): Flow<Company> =
        companyRepository.findAll()

    suspend fun findByName(name: String): Flow<Company> =
        companyRepository.findByNameContaining(name)

    suspend fun saveCompany(company: Company): Company =
        companyRepository.save(company)

    suspend fun deleteCompanyById(id: Long) {
        companyRepository.findById(id)?.let {
            companyRepository.deleteById(id)
        } ?: ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    suspend fun updateCompanyById(id: Long, requestedCompany: Company): Company =
        companyRepository.findById(id)?.let {
            companyRepository.save(requestedCompany.copy(id = it.id))
        } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
}