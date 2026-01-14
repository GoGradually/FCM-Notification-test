package me.gogradually.fcmnotificationtest.infrastrcuture

import me.gogradually.fcmnotificationtest.application.PushService
import me.gogradually.fcmnotificationtest.domain.PushSubscription
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import me.gogradually.fcmnotificationtest.external.MockFcmProcessor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * V3 + 가상 스레드 도입 버전
 */
@Service
class MockFcmServiceV4(
    private val mockFcmProcessor: MockFcmProcessor,
    private val pushSubscriptionRepository: PushSubscriptionRepository
) :
    PushService {

    val threadPoolExecutor = Executors.newVirtualThreadPerTaskExecutor()
    val logger = LoggerFactory.getLogger(MockFcmServiceV4::class.java)


    override fun sendPushMessage(token: String, ownerId: Long) {
        mockFcmProcessor.sendMessage(token, ownerId)
    }


    fun sendNotificationToOwner(ownerId: Long): MutableList<Future<*>> {
        val subscriptions =
            pushSubscriptionRepository.findAllByMemberId(ownerId)

        val futures = mutableListOf<Future<*>>()

        subscriptions.forEach { subscription: PushSubscription ->
            futures.add(
                threadPoolExecutor.submit {
                    sendPushMessage(subscription.token, ownerId)
                }
            )
        }

        return futures
    }
}
