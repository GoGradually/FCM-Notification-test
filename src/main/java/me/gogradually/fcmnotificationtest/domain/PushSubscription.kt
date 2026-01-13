package me.gogradually.fcmnotificationtest.domain

import jakarta.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(name = "uk_memberId_deviceId", columnNames = ["member_id", "device_id"])])
class PushSubscription(
    memberId: Long,
    deviceId: String,
    token: String
) {
    @Id
    var id: Long? = null

    @Column(name = "member_id", nullable = false)
    var memberId: Long = memberId

    @Column(name = "device_id", nullable = false)
    var deviceId: String = deviceId

    @Column(name = "token", nullable = false)
    var token: String = token

}