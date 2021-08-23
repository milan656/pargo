package com.tntra.pargo2.model.comments

data class CommentListModel(
        val comment: Comment,
        val message: String,
        val success: Boolean,
        var commentName: String,
        var commentImage: String,
        var commentDateTime: String,
        var type: Int
)