package com.tntra.pargo.model.comments.list

data class CommentsListingModel(
    val comments: List<Comment>,
    val message: String,
    val success: Boolean
)