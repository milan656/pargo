package com.tntra.pargo2.model.comments

data class Attributes(
    val content_id: Int,
    val message: String,
    val user: User
)