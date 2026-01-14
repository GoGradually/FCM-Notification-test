package me.gogradually.fcmnotificationtest.application

import me.gogradually.fcmnotificationtest.common.LogElapsedTime
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import me.gogradually.fcmnotificationtest.infrastrcuture.MockFcmServiceV1
import org.springframework.stereotype.Service
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Service
class NotificationSchedulerV1(
    private val pushSubscriptionRepository: PushSubscriptionRepository, private val pushService: MockFcmServiceV1
) {

    val threadPoolExecutor = ThreadPoolExecutor(100, 100, 60L, TimeUnit.SECONDS, LinkedBlockingQueue())

    init {
        threadPoolExecutor.allowCoreThreadTimeOut(true)
    }

    @LogElapsedTime
    fun dispatchDueNotifications() {
        val dueNotificationOwners = (0 until 2000L).toList()

        val futures = mutableListOf<Future<*>>()

        dueNotificationOwners.forEach {
            futures.add(threadPoolExecutor.submit {
                pushService.sendNotificationToOwner(it)
            })
        }

        futures.forEach { it.get() }
    }
}