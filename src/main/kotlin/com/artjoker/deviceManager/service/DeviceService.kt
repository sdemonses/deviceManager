package com.artjoker.deviceManager.service

import com.artjoker.deviceManager.controller.GetAllRequest
import com.artjoker.deviceManager.exceptions.DeviceNotFoundException
import com.artjoker.deviceManager.model.Device
import com.artjoker.deviceManager.repository.DeviceCrudRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime
import javax.transaction.Transactional
import kotlin.streams.toList

@Service
class DeviceService(
    private val deviceCrudRepository: DeviceCrudRepository,
    private val clock: Clock
) {
    fun createDevice(createDeviceInput: CreateDeviceInput): String {
        val creationDate = LocalDateTime.now(clock)
        val device = Device(
            userId = createDeviceInput.userId,
            deviceId = createDeviceInput.deviceId,
            os = createDeviceInput.os,
            creationDate = creationDate,
            updatingTime = creationDate
        )
        return deviceCrudRepository.save(device).deviceId
    }

    @Transactional
    fun removeDevice(deviceId: String, userId: String) {
        checkDeviceBelongTocCustomer(deviceId, userId)
        deviceCrudRepository.deleteDeviceByDeviceId(deviceId).takeIf { it > 0L } ?: throw DeviceNotFoundException()
    }

    @Transactional
    fun updateDevice(updateDeviceInput: UpdateDeviceInput) {
        checkDeviceBelongTocCustomer(updateDeviceInput.deviceId, updateDeviceInput.userId)
        deviceCrudRepository.findByIdOrNull(updateDeviceInput.deviceId).run {
            deviceCrudRepository.save(
                this!!.copy(
                    os = updateDeviceInput.os,
                    updatingTime = LocalDateTime.now(clock)
                )
            )
        }

    }

    fun getAll(getAllRequest: GetAllRequest): List<Device> =
        deviceCrudRepository.findAll(PageRequest.of(getAllRequest.offset / getAllRequest.limit, getAllRequest.limit))
            .get().toList()

    private fun checkDeviceBelongTocCustomer(deviceId: String, userId: String) =
        deviceCrudRepository.getDeviceByDeviceIdAndUserId(deviceId, userId) ?: throw DeviceNotFoundException()

}

data class CreateDeviceInput(val deviceId: String, val os: String, val userId: String)
data class UpdateDeviceInput(val deviceId: String, val os: String, val userId: String)
