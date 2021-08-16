package com.tntra.pargo.model.comments

data class CommentListModel(
        val comment: Comment,
        val message: String,
        val success: Boolean,
        var commentName: String,
        var commentImage: String,
        var commentDateTime: String,
        var type: Int
)