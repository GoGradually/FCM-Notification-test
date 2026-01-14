package me.gogradually.fcmnotificationtest.application

import me.gogradually.fcmnotificationtest.common.LogElapsedTime
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import me.gogradually.fcmnotificationtest.infrastrcuture.MockFcmServiceV5
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.Future
import kotlin.system.measureTimeMillis

/**
 * V4 + 정확한 단일 작업 처리 시간 로깅을 위한 로그 추가 버전
 */
@Service
class NotificationSchedulerV5(
    private val pushSubscriptionRepository: PushSubscriptionRepository, private val pushService: MockFcmServiceV5
) {

    val logger = LoggerFactory.getLogger(NotificationSchedulerV5::class.java)

    @LogElapsedTime
    @Transactional
    fun dispatchDueNotifications() {
        val dueNotificationOwners = (0 until 1L).toList()
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