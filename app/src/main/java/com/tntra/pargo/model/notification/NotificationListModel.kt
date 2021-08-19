package com.tntra.pargo.model.notification

data class NotificationListModel(
    val message: String,
    val notifications: List<Notification>,
    val success: Boolean
)