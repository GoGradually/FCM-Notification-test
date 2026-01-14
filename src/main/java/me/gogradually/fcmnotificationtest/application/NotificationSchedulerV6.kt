package me.gogradually.fcmnotificationtest.application

import me.gogradually.fcmnotificationtest.common.LogElapsedTime
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import me.gogradually.fcmnotificationtest.infrastrcuture.MockFcmServiceV6
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.system.measureTimeMillis

/**
 * V4 + 수신 기기 조회 배치 쿼리 버전
 */
@Service
class NotificationSchedulerV6(
    private val pushSubscriptionRepository: PushSubscriptionRepository, private val pushService: MockFcmServiceV6
) {

    val logger = LoggerFactory.getLogger(NotificationSchedulerV6::class.java)

    @LogElapsedTime
    @Transactional
    fun dispatchDueNotifications() {
        val dueNotificationOwners = (0 until 200_000L).toList()
        val tokens = mutableListOf<String>()
        val measureTimeMillis = measureTimeMillis {

            val pushReceivers = pushSubscriptionRepository.findAllByMemberIdIn(dueNotificationOwners)

            val messages = pushReceivers.map {
                MockFcmServiceV6.Message(it.token, it.memberId)
            }.toList()

            val futures = pushService.sendNotificationsEachOwner(messages)

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