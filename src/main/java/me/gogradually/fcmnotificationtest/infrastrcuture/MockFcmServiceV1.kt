package me.gogradually.fcmnotificationtest.infrastrcuture

import me.gogradually.fcmnotificationtest.application.PushService
import me.gogradually.fcmnotificationtest.domain.PushSubscription
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import me.gogradually.fcmnotificationtest.external.MockFcmProcessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class MockFcmServiceV1(
    private val mockFcmProcessor: MockFcmProcessor,
    private val pushSubscriptionRepository: PushSubscriptionRepository
) :
    PushService {


    override fun sendPushMessage(token: String, ownerId: Long) {
        try {
            mockFcmProcessor.sendMessage(token, ownerId)
        } catch (e: Exception) {
            pushSubscriptionRepository.deleteByToken(token)
        }
    }


    @Transactional
    fun sendNotificationToOwner(ownerId: Long) {
        val subscriptions =
            pushSubscriptionRepository.findAllByMemberId(ownerId)

        subscriptions.forEach { subscription: PushSubscription ->
            sendPushMessage(subscription.token, ownerId)
        }
    }
}
