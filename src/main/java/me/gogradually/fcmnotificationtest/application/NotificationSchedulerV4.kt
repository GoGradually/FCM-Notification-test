package me.gogradually.fcmnotificationtest.application

import me.gogradually.fcmnotificationtest.common.LogElapsedTime
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import me.gogradually.fcmnotificationtest.infrastrcuture.MockFcmServiceV4
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.Future
import kotlin.system.measureTimeMillis

/**
 * V3 + 가상 스레드 도입 버전
 */
@Service
class NotificationSchedulerV4(
    private val pushSubscriptionRepository: PushSubscriptionRepository, private val pushService: MockFcmServiceV4
) {

    val logger = LoggerFactory.getLogger(NotificationSchedulerV4::class.java)

    @LogElapsedTime
    @Transactional
    fun dispatchDueNotifications() {
        val dueNotificationOwners = (0 until 2000L).toList()
        val tokens = mutableListOf<String>()
        val measureTimeMillis = measureTimeMillis {
            val futures = mutableListOf<Future<*>>()

            dueNotificationOwners.forEach {
                futures.addAll(pushService.sendNotificationToOwner(it))
            }

            for (future in futures) {
                try {
                    future.get()
                } catch (e: Exception) {
                    val failedToken = e.cause?.message ?: continue
                    tokens.add(failedToken)
                }
            }
        }

        logger.info("Total dispatch time: {} ms", measureTimeMillis)

        pushSubscriptionRepository.deleteAllByTokenInBatch(tokens)
    }
}