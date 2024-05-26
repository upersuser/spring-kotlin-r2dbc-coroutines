package dev.upersuser.companycrud.controller

import dev.upersuser.companycrud.dto.CompanyRequest
import dev.upersuser.companycrud.dto.CompanyResponse
import dev.upersuser.companycrud.dto.UserResponse
import dev.upersuser.companycrud.model.Company
import dev.upersuser.companycrud.service.CompanyService
import dev.upersuser.companycrud.service.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toList
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
@RequestMapping("/companies")
class CompanyController(
    private val companyService: CompanyService,
    private val userService: UserService
) {

    @PostMapping
    suspend fun createCompany(
        @RequestBody request: CompanyRequest
    ): CompanyResponse =
        companyService.saveCompany(request.toCompany()).toCompanyResponse()
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)

    @GetMapping
    suspend fun getCompanies(
        @RequestParam("name", required = false) name: String?
    ): Flow<CompanyResponse> =
        (name?.let { companyService.findByName(name) } ?: companyService.findAllCompanies())
            .mapNotNull { it.toCompanyResponse() }

    @GetMapping("/{id}")
    suspend fun getCompanyById(
        @PathVariable("id") id: Long
    ): CompanyResponse =
        companyService.findById(id)?.toCompanyResponse() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PutMapping("/{id}")
    suspend fun updateCompanyById(
        @PathVariable("id") id: Long,
        @RequestBody request: CompanyRequest
    ): CompanyResponse =
        companyService.updateCompanyById(id, request.toCompany()).toCompanyResponse()
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)

    @DeleteMapping("/{id}")
    suspend fun deleteCompanyById(
        @PathVariable("id") id: Long
    ) { companyService.deleteCompanyById(id) }

    private suspend fun Company.toCompanyResponse(): CompanyResponse? =
        id?.let { companyId ->
            CompanyResponse.fromCompany(
                this,
                userService.findByCompanyId(companyId).mapNotNull { UserResponse.fromUser(it) }.toList()
            )
        }
}

