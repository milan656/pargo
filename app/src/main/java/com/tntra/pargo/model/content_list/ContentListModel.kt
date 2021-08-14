package com.tntra.pargo.model.content_list

data class ContentListModel(
    val contents: List<Content>,
    val message: String,
    val success: Boolean
)