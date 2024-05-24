package dev.upersuser.spring.rest.dto

import dev.upersuser.spring.rest.model.User

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val age: Int
) {
    companion object {
        fun fromUser(user: User): UserResponse? = user.id?.let { UserResponse(it, user.name, user.email, user.age) }
    }
}
