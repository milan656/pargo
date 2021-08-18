package com.tntra.pargo.model.followers

data class Follow(
    val attributes: AttributesX,
    val id: String,
    val type: String,
    var isChecked: Boolean,
    var name: String,
    var passion: String
)