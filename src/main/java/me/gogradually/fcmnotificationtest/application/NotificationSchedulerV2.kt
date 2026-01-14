package me.gogradually.fcmnotificationtest.application

import me.gogradually.fcmnotificationtest.common.LogElapsedTime
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import me.gogradually.fcmnotificationtest.infrastrcuture.MockFcmServiceV2
import org.springframework.stereotype.Service
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * V1 + 스레드 풀을 늘린 버전
 */
@Service
class NotificationSchedulerV2(
    private val pushSubscriptionRepository: PushSubscriptionRepository, private val pushService: MockFcmServiceV2
) {

    val threadPoolExecutor = ThreadPoolExecutor(500, 500, 60L, TimeUnit.MINUTES, LinkedBlockingQueue())

    init {
        threadPoolExecutor.allowCoreThreadTimeOut(true)
    }

    @LogElapsedTime
    fun dispatchDueNotifications() {
        val dueNotificationOwners = (0 until 2_000L).toList()

        val futures = mutableListOf<Future<*>>()

        dueNotificationOwners.forEach {
            futures.add(threadPoolExecutor.submit {
                pushService.sendNotificationToOwner(it)
            })
        }

        futures.forEach { it.get() }
    }
}