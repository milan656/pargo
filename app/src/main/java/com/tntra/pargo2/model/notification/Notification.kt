package com.tntra.pargo2.model.notification

data class Notification(
    val attributes: Attributes,
    val id: String,
    val type: String,
    var addressTitle: String,
    var fullAddress: String,
    var date: String,
    var building_uuid: String,
    var dateFormated: String,
    var createdAt: Long = 0,
    val updatedAt: Long = 0,
    val requestAccepted: Boolean
)