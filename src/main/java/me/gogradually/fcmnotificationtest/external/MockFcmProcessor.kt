package me.gogradually.fcmnotificationtest.external

import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import kotlin.time.Duration.Companion.milliseconds

@Service
class MockFcmProcessor {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun sendMessage(token: String, ownerId: Long) {
        runBlocking {
            receiveMessage(token, ownerId)
        }
    }

    private suspend fun receiveMessage(token: String, ownerId: Long) {
        scope.async {
            delay(100.milliseconds)
            if (ownerId % 100L == 0L) {
                throw Exception(token)
            }
            ownerId
        }.await()
    }
}

/**
 * 두 가지 목표
 * 1. FCM 메시지를 수신하고, 응답하는 기능을 모방
 * 2. 현재 notification 서버와 대비되는, 높은 동시성 처리 능력
 * 이벤트 루프가 필요함
 */