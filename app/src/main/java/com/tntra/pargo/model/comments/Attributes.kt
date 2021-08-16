package com.tntra.pargo.model.comments

data class Attributes(
    val content_id: Int,
    val message: String,
    val user: User
)