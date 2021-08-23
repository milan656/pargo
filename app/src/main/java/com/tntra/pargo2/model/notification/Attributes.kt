package com.tntra.pargo2.model.notification

data class Attributes(
        val body: String,
        val `data`: Data,
        val read: Any,
        val title: String,
        val created_at: String
)