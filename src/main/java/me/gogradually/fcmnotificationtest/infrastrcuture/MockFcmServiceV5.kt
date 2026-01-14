package me.gogradually.fcmnotificationtest.infrastrcuture

import me.gogradually.fcmnotificationtest.application.PushService
import me.gogradually.fcmnotificationtest.domain.PushSubscription
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import me.gogradually.fcmnotificationtest.external.MockFcmProcessor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.system.measureTimeMillis

/**
 * V4 + 정확한 단일 작업 처리 시간 로깅을 위한 로그 추가 버전
 */
@Service
class MockFcmServiceV5(
    private val mockFcmProcessor: MockFcmProcessor,
    private val pushSubscriptionRepository: PushSubscriptionRepository
) :
    PushService {

    val threadPoolExecutor = Executors.newVirtualThreadPerTaskExecutor()
    val logger = LoggerFactory.getLogger(MockFcmServiceV5::class.java)


    override fun sendPushMessage(token: String, ownerId: Long) {
        mockFcmProcessor.sendMessage(token, ownerId)
    }


    fun sendNotificationToOwner(ownerId: Long): MutableList<Future<*>> {

        val getSubscriptionTime = measureTimeMillis {
            pushSubscriptionRepository.findAllByMemberId(ownerId)
        }
        logger.info("Get subscription time: {} ms", getSubscriptionTime)
        val subscriptions = pushSubscriptionRepository.findAllByMemberId(ownerId)

        val futures = mutableListOf<Future<*>>()

        val getPushMessageTime = measureTimeMillis {
            subscriptions.forEach { subscription: PushSubscription ->
                futures.add(
                    threadPoolExecutor.submit {
                        sendPushMessage(subscription.token, ownerId)
                    }
                )
            }
        }

        logger.info("Get push message time: {} ms", getPushMessageTime)

        return futures
    }
}
