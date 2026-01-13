package me.gogradually.fcmnotificationtest.domain

import org.springframework.data.jpa.repository.JpaRepository

interface PushSubscriptionRepository: JpaRepository<PushSubscription, Long> {
    fun deleteByToken(token: String?): Long
    fun findAllByMemberId(memberId: Long): List<PushSubscription>
}