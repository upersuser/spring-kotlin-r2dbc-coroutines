package dev.upersuser.spring.rest.dto

import dev.upersuser.spring.rest.model.Company


data class CompanyRequest(
    val name: String,
    val address: String
) {
    fun toCompany(): Company = Company(name = name, address = address)
}
