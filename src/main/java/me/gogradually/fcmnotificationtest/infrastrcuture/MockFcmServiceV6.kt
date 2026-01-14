package me.gogradually.fcmnotificationtest.infrastrcuture

import me.gogradually.fcmnotificationtest.application.PushService
import me.gogradually.fcmnotificationtest.domain.PushSubscriptionRepository
import me.gogradually.fcmnotificationtest.external.MockFcmProcessor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * V4 + 수신 기기 조회 배치 쿼리 버전
 */
@Service
class MockFcmServiceV6(
    private val mockFcmProcessor: MockFcmProcessor,
    private val pushSubscriptionRepository: PushSubscriptionRepository
) :
    PushService {

    val threadPoolExecutor = Executors.newVirtualThreadPerTaskExecutor()
    val logger = LoggerFactory.getLogger(MockFcmServiceV6::class.java)


    override fun sendPushMessage(token: String, ownerId: Long) {
        mockFcmProcessor.sendMessage(token, ownerId)
    }


    fun sendNotificationsEachOwner(messages: List<Message>): MutableList<Future<*>> {

        val futures = mutableListOf<Future<*>>()

        messages.forEach { message: Message ->
            futures.add(
                threadPoolExecutor.submit {
                    sendPushMessage(message.token, message.ownerId)
                }
            )
        }

        return futures
    }

    public data class Message(val token: String, val ownerId: Long)
}
