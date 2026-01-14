package me.gogradually.fcmnotificationtest.infrastrcuture

import me.gogradually.fcmnotificationtest.application.PushService
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import me.gogradually.fcmnotificationtest.external.MockFcmProcessor
import org.springframework.stereotype.Service


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
}
