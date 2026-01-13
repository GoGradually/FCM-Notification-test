package me.gogradually.fcmnotificationtest.interfaces

import me.gogradually.fcmnotificationtest.application.NotificationScheduler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PushController(private val notificationScheduler: NotificationScheduler) {
    @GetMapping("/test")
    fun test() {
        notificationScheduler.dispatchDueNotifications()
    }

}