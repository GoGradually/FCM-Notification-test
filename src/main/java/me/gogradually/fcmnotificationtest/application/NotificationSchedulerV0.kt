package me.gogradually.fcmnotificationtest.application

import me.gogradually.fcmnotificationtest.domain.PushSubscription
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationSchedulerV0(
    private val pushSubscriptionRepository: PushSubscriptionRepository,
    private val pushService: PushService
) {

    @Transactional
    fun dispatchDueNotifications() {
        val dueNotificationOwners = (0 until 2000L).toList()

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