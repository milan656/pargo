package com.tntra.pargo2.model.followers

data class Follow(
    val attributes: Attributes,
    val id: String,
    val type: String,
    var isChecked:Boolean
)