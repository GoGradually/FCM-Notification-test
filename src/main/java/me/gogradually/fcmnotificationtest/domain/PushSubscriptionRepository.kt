package me.gogradually.fcmnotificationtest.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface PushSubscriptionRepository: JpaRepository<PushSubscription, Long> {
    fun deleteByToken(token: String?): Long
    fun findAllByMemberId(memberId: Long): List<PushSubscription>
    fun findAllByMemberIdIn(memberIds: List<Long>): List<PushSubscription>

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from PushSubscription e where e.token in :tokens")
    fun deleteAllByTokenInBatch(tokens: List<String>): Long
}