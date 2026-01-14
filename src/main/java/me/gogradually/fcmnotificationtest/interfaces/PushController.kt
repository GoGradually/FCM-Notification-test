package me.gogradually.fcmnotificationtest.interfaces

import me.gogradually.fcmnotificationtest.application.NotificationSchedulerV0
import me.gogradually.fcmnotificationtest.application.NotificationSchedulerV1
import me.gogradually.fcmnotificationtest.application.NotificationSchedulerV2
import me.gogradually.fcmnotificationtest.application.NotificationSchedulerV3
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class PushController(
    private val notificationSchedulerV0: NotificationSchedulerV0,
    private val notificationSchedulerV1: NotificationSchedulerV1,
    private val notificationSchedulerV2: NotificationSchedulerV2,
    private val notificationSchedulerV3: NotificationSchedulerV3
) {
    @GetMapping("/v0")
    fun testV0() {
        notificationSchedulerV0.dispatchDueNotifications()
    }

    @GetMapping("/v1")
    fun testV1() {
        notificationSchedulerV1.dispatchDueNotifications()
    }

    @GetMapping("/v2")
    fun testV2() {
        notificationSchedulerV2.dispatchDueNotifications()
    }

    @GetMapping("/v3")
    fun testV3() {
        notificationSchedulerV3.dispatchDueNotifications()
    }
}