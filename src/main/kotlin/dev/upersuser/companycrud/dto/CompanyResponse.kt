package dev.upersuser.companycrud.dto

import dev.upersuser.companycrud.model.Company


data class CompanyResponse(
    val id: Long,
    val name: String,
    val address: String,
    val users: List<UserResponse>
) {
    companion object {
        fun fromCompany(company: Company, users: List<UserResponse> = emptyList()): CompanyResponse? =
            company.id?.let { CompanyResponse(id = it, name = company.name, address = company.address, users = users) }
    }
}

