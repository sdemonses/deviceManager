package com.artjoker.deviceManager.controller

import com.artjoker.deviceManager.exceptions.DeviceNotFoundException
import com.artjoker.deviceManager.exceptions.JwtTokenInvalidException
import com.artjoker.deviceManager.exceptions.NotAuthorisedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionController {
    @ExceptionHandler(DeviceNotFoundException::class)
    fun handle(exception: DeviceNotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity(ErrorResponse("Device was not found"), HttpStatus.NOT_FOUND)

    @ExceptionHandler(NotAuthorisedException::class)
    fun handle(exception: NotAuthorisedException): ResponseEntity<ErrorResponse> =
        ResponseEntity(ErrorResponse("You have no permissions to perform this action"), HttpStatus.FORBIDDEN)

    @ExceptionHandler(JwtTokenInvalidException::class)
    fun handle(exception: JwtTokenInvalidException): ResponseEntity<ErrorResponse> =
        ResponseEntity(ErrorResponse("JWT token is invalid"), HttpStatus.BAD_REQUEST)
}

data class ErrorResponse(
    val code: String
)