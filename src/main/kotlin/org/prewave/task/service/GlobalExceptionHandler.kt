package org.prewave.task.service

import org.jooq.exception.DataAccessException
import org.prewave.task.entity.dto.ErrorResponse
import org.prewave.task.exception.EntityAlreadyExistsException
import org.prewave.task.exception.EntityNotFoundException
import org.prewave.task.exception.InternalServerErrorException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.stream.Collectors

@ControllerAdvice
class GlobalExceptionHandler: ResponseEntityExceptionHandler() {


    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorMsg = ex.bindingResult.fieldErrors
            .map { obj -> "${obj.field} ${obj.defaultMessage}"}
            .joinToString("\n")
        return ResponseEntity(ErrorResponse(errorMsg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<Any> {
        log.warn(e.message)
        return ResponseEntity(ErrorResponse(e.message!!), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(EntityAlreadyExistsException::class)
    fun handleEntityAlreadyExistsException(e: EntityAlreadyExistsException): ResponseEntity<Any> {
        log.warn(e.message)
        return ResponseEntity(ErrorResponse(e.message!!), HttpStatus.CONFLICT)
    }

    @ExceptionHandler(InternalServerErrorException::class)
    fun handleInternalServerErrorException(e: InternalServerErrorException): ResponseEntity<Any> {
        log.warn(e.message)
        return ResponseEntity(ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): ResponseEntity<Any> {
        log.warn(e.message)
        return ResponseEntity(ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
