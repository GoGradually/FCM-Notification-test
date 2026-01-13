package me.gogradually.fcmnotificationtest.application

interface PushService {
    fun sendPushMessage(token: String, ownerId: Long)
}