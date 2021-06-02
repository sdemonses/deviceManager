package com.artjoker.deviceManager.service

import com.artjoker.deviceManager.exceptions.DeviceNotFoundException
import com.artjoker.deviceManager.model.Device
import com.artjoker.deviceManager.repository.DeviceCrudRepository
import com.artjoker.deviceManager.stubbedCreateDeviceInput
import com.artjoker.deviceManager.stubbedDevice
import com.artjoker.deviceManager.stubbedUpdateDeviceInput
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

internal class DeviceServiceTest {

    private val deviceCrudRepository = mockk<DeviceCrudRepository>()
    private val clock = Clock.fixed(Instant.parse("2021-04-30T18:00:00Z"), ZoneId.of("Europe/London"))

    private val deviceService = DeviceService(deviceCrudRepository, clock)

    @Test
    fun `should create new device`() {
        val createDeviceInput = stubbedCreateDeviceInput()
        val expectedDevice = Device(
            createDeviceInput.deviceId,
            createDeviceInput.userId,
            createDeviceInput.os,
            LocalDateTime.now(clock),
            LocalDateTime.now(clock)
        )
        every { deviceCrudRepository.save(expectedDevice) } returns expectedDevice


        deviceService.createDevice(createDeviceInput)

        verify {
            deviceCrudRepository.save(expectedDevice)
        }


    }

    @Test
    fun `should remove device`() {
        val deviceId = "deviceId"
        val userId = "userId"
        every { deviceCrudRepository.getDeviceByDeviceIdAndUserId(deviceId, userId) } returns stubbedDevice()
        every { deviceCrudRepository.deleteDeviceByDeviceId(deviceId) } returns 1L

        deviceService.removeDevice(deviceId, userId)

        verifyAll {
            deviceCrudRepository.getDeviceByDeviceIdAndUserId(deviceId, userId)
            deviceCrudRepository.deleteDeviceByDeviceId(deviceId)
        }

    }

    @Test
    fun `should throw DeviceNotFoundException if that not exist or not belongs to customer`() {
        val deviceId = "deviceId"
        val userId = "userId"
        every { deviceCrudRepository.getDeviceByDeviceIdAndUserId(deviceId, userId) } returns null

        { deviceService.removeDevice(deviceId, userId) }
            .shouldThrow(DeviceNotFoundException::class)

    }

    @Test
    fun `should throw DeviceNotFoundException if that not belongs to customer`() {
        val deviceId = "deviceId"
        val userId = "userId"
        every { deviceCrudRepository.getDeviceByDeviceIdAndUserId(deviceId, userId) } returns null

        { deviceService.updateDevice(stubbedUpdateDeviceInput()) }
            .shouldThrow(DeviceNotFoundException::class)
    }

    @Test
    fun getAll() {
    }
}
