package com.tntra.pargo.model.treading_content

data class TreadingContentModel(
    val contents: List<Content>,
    val message: String,
    val success: Boolean
)