package com.artjoker.deviceManager.controller

import com.artjoker.deviceManager.model.Device
import com.artjoker.deviceManager.service.CreateDeviceInput
import com.artjoker.deviceManager.service.DeviceService
import com.artjoker.deviceManager.service.UpdateDeviceInput
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/")
class DeviceController(private val deviceService: DeviceService) {

    @GetMapping("devices")
    fun getAll(getAllRequest: GetAllRequest): ResponseEntity<List<Device>> =
        ResponseEntity(deviceService.getAll(getAllRequest), OK)


    @PostMapping("customers/{userId}/devices")
    @PreAuthorize("authentication.principal == #userId")
    fun createDevice(
        @PathVariable("userId") userId: String,
        @RequestBody createDeviceRequest: CreateDeviceRequest
    ): ResponseEntity<String> =
        deviceService.createDevice(createDeviceRequest.toCreateDeviceInput(userId))
            .run { ResponseEntity(this, CREATED) }


    @DeleteMapping("customers/{userId}/devices/{deviceId}")
    @PreAuthorize("authentication.principal == #userId")
    @ResponseStatus(NO_CONTENT)
    fun removeDevice(
        @PathVariable("userId") userId: String,
        @PathVariable("deviceId") deviceId: String,
    ) =
        deviceService.removeDevice(deviceId, userId)


    @PutMapping("customers/{userId}/devices/{deviceId}")
    @PreAuthorize("authentication.principal == #userId")
    fun updateDevice(
        @PathVariable("userId") userId: String,
        @PathVariable("deviceId") deviceId: String,
        @RequestBody updateDeviceRequest: UpdateDeviceRequest
    ): ResponseEntity<Any> =
        deviceService.updateDevice(
            UpdateDeviceInput(
                deviceId, updateDeviceRequest.os, userId
            )
        ).run { ResponseEntity(NO_CONTENT) }


}

data class CreateDeviceRequest(val deviceId: String, val os: String) {
    fun toCreateDeviceInput(userId: String) = CreateDeviceInput(
        deviceId = deviceId,
        os = os,
        userId = userId
    )
}

data class UpdateDeviceRequest(val os: String)

data class GetAllRequest(val limit: Int = 5, val offset: Int = 0)
