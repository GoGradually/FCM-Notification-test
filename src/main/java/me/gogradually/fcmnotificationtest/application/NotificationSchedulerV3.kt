package me.gogradually.fcmnotificationtest.application

import me.gogradually.fcmnotificationtest.common.LogElapsedTime
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import me.gogradually.fcmnotificationtest.infrastrcuture.MockFcmServiceV3
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.Future

/**
 * V1 + 배치 처리로 네트워크 I/O 횟수를 줄인 버전
 */
@Service
class NotificationSchedulerV3(
    private val pushSubscriptionRepository: PushSubscriptionRepository, private val pushService: MockFcmServiceV3
) {

    @LogElapsedTime
    @Transactional
    fun dispatchDueNotifications() {
        val dueNotificationOwners = (0 until 2000L).toList()

        val futures = mutableListOf<Future<*>>()

        dueNotificationOwners.forEach {
            futures.addAll(pushService.sendNotificationToOwner(it))
        }

        val tokens = mutableListOf<String>()
        for (future in futures) {
            try {
                future.get()
            } catch (e: Exception) {
                val failedToken = e.cause?.message ?: continue
                tokens.add(failedToken)
            }
        }

        pushSubscriptionRepository.deleteAllByTokenInBatch(tokens)
    }
}