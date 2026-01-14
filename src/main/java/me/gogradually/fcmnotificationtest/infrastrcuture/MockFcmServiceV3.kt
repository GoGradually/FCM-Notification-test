package me.gogradually.fcmnotificationtest.infrastrcuture

import me.gogradually.fcmnotificationtest.application.PushService
import me.gogradually.fcmnotificationtest.domain.PushSubscription
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import me.gogradually.fcmnotificationtest.external.MockFcmProcessor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * V1 + 배치 처리로 네트워크 I/O 횟수를 줄인 버전
 */
@Service
class MockFcmServiceV3(
    private val mockFcmProcessor: MockFcmProcessor,
    private val pushSubscriptionRepository: PushSubscriptionRepository
) :
    PushService {

    val threadPoolExecutor = ThreadPoolExecutor(100, 100, 60L, TimeUnit.SECONDS, LinkedBlockingQueue())
    val logger = LoggerFactory.getLogger(MockFcmServiceV3::class.java)

    init {
        threadPoolExecutor.allowCoreThreadTimeOut(true)
    }

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
