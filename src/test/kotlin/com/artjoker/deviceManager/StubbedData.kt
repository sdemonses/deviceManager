package com.artjoker.deviceManager

import com.artjoker.deviceManager.model.Device
import com.artjoker.deviceManager.service.CreateDeviceInput
import com.artjoker.deviceManager.service.UpdateDeviceInput
import java.time.LocalDateTime

fun stubbedCreateDeviceInput(
    deviceId: String = "deviceId",
    os: String = "os",
    userId: String = "userId"
) =
    CreateDeviceInput(deviceId, os, userId)

fun stubbedUpdateDeviceInput(
    deviceId: String = "deviceId",
    os: String = "os",
    userId: String = "userId"
) =
    UpdateDeviceInput(deviceId, os, userId)


fun stubbedDevice(
    deviceId: String = "deviceId",
    userId: String = "userId",
    os: String = "os",
    creationTime: LocalDateTime = LocalDateTime.now(),
    updatingTime: LocalDateTime = LocalDateTime.now()
) = Device(
    deviceId,
    userId,
    os,
    creationTime,
    updatingTime
)