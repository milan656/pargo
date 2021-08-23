package com.tntra.pargo2.model.content_list

data class ContentListModel(
    val contents: List<Content>,
    val message: String,
    val success: Boolean
)