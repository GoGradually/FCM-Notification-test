package me.gogradually.fcmnotificationtest.interfaces

import me.gogradually.fcmnotificationtest.application.NotificationSchedulerV0
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class PushController(private val notificationSchedulerV0: NotificationSchedulerV0) {
    @GetMapping("/v0")
    fun test() {
        notificationSchedulerV0.dispatchDueNotifications()
    }

}