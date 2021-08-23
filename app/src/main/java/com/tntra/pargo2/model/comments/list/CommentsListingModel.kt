package com.tntra.pargo2.model.comments.list

data class CommentsListingModel(
    val comments: List<Comment>,
    val message: String,
    val success: Boolean
)