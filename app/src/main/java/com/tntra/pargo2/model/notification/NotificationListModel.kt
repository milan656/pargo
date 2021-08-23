package com.tntra.pargo2.model.notification

data class NotificationListModel(
    val message: String,
    val notifications: List<Notification>,
    val success: Boolean
)