package com.tntra.pargo2.model.generes

data class GeneresListModel(
    val genres: List<Genre>,
    val message: String,
    val success: Boolean
)