package me.gogradually.fcmnotificationtest.common

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.system.measureNanoTime

@Aspect
@Component
class LogElapsedTimeAspect {
    private val logger = LoggerFactory.getLogger(LogElapsedTimeAspect::class.java)

    @Around("@annotation(me.gogradually.fcmnotificationtest.common.LogElapsedTime)")
    fun logElapsedTime(joinPoint: ProceedingJoinPoint): Any? {
        var result: Any? = null
        val elapsedNanos = measureNanoTime {
            result = joinPoint.proceed()
        }
        val signature = joinPoint.signature.toShortString()
        val elapsedMs = elapsedNanos / 1_000_000.0
        logger.info("elapsedMs={} signature={} response={}", String.format("%.3f", elapsedMs), signature, result)
        return result
    }
}
