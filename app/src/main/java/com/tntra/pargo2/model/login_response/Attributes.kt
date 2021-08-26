package com.tntra.pargo2.model.login_response

data class Attributes(
        val email: String,
        val profile_img_path: String,
        val profile: Profile,
        val roles: List<Role>
)