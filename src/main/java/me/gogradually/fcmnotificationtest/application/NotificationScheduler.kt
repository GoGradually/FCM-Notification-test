package me.gogradually.fcmnotificationtest.application

import me.gogradually.fcmnotificationtest.domain.PushSubscription
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.function.Consumer

@Service
class NotificationScheduler(
    private val pushSubscriptionRepository: PushSubscriptionRepository,
    private val pushService: PushService
) {

    val log = org.slf4j.LoggerFactory.getLogger(NotificationScheduler::class.java)

    @Transactional
    fun dispatchDueNotifications() {
        val dueNotificationOwners = (0 until 100000L).toList()

        dueNotificationOwners.forEach {
            owner -> sendNotificationToOwner(owner)
        }
    }


    private fun sendNotificationToOwner(ownerId: Long) {
        val subscriptions =
            pushSubscriptionRepository.findAllByMemberId(ownerId)

        subscriptions.forEach { subscription: PushSubscription ->
            pushService.sendPushMessage(subscription.token, ownerId)
        }
    }
}