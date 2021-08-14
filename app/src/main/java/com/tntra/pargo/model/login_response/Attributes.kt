package com.tntra.pargo.model.login_response

data class Attributes(
    val email: String,
    val profile: Profile,
    val roles: List<Role>
)