package me.gogradually.fcmnotificationtest.interfaces

import me.gogradually.fcmnotificationtest.application.NotificationScheduler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class PushController(private val notificationScheduler: NotificationScheduler) {
    @GetMapping("/v0")
    fun test() {
        notificationScheduler.dispatchDueNotifications()
    }

}