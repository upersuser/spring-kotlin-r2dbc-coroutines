package dev.upersuser.companycrud.dto

import dev.upersuser.companycrud.model.User

data class UserRequest(
    val name: String,
    val email: String,
    val age: Int,
    val companyId: Long
) {
    fun toUser(): User = User(email = email, name = name, age = age, companyId = companyId)
}
