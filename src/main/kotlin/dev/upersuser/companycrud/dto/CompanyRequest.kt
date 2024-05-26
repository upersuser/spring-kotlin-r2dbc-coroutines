package dev.upersuser.companycrud.dto

import dev.upersuser.companycrud.model.Company


data class CompanyRequest(
    val name: String,
    val address: String
) {
    fun toCompany(): Company = Company(name = name, address = address)
}
