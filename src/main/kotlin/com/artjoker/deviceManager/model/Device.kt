package com.artjoker.deviceManager.model

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "devices")
data class Device(
    @Id
    @Column(name = "deviceId")
    val deviceId: String,
    @Column(name = "userId")
    val userId: String,
    @Column(name = "os")
    val os: String,
    @Column(name = "creationDate")
    val creationDate: LocalDateTime,
    @Column(name = "updatingTime")
    var updatingTime: LocalDateTime
)