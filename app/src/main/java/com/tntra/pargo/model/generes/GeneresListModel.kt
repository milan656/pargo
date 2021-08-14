package com.tntra.pargo.model.generes

data class GeneresListModel(
    val genres: List<Genre>,
    val message: String,
    val success: Boolean
)