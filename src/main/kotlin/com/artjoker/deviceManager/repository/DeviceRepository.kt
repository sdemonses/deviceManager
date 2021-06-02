package com.artjoker.deviceManager.repository

import com.artjoker.deviceManager.model.Device
import org.springframework.data.jpa.repository.JpaRepository

interface DeviceCrudRepository : JpaRepository<Device, String> {
    fun deleteDeviceByDeviceId(deviceId: String): Long
    fun getDeviceByDeviceIdAndUserId(deviceId: String, userId: String): Device?
}