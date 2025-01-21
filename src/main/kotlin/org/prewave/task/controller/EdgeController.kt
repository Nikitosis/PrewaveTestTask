package org.prewave.task.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.prewave.task.entity.dto.EdgeDTO
import org.prewave.task.service.EdgeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/edges")
class EdgeController(
    private val edgeService: EdgeService
) {
    @PostMapping
    fun createEdge(@Valid @RequestBody edgeDTO: EdgeDTO): ResponseEntity<Any> {
        edgeService.createEdge(edgeDTO)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping
    fun removeEdge(@RequestParam fromId: Int, @RequestParam toId: Int): ResponseEntity<Any> {
        edgeService.removeEdge(fromId, toId)
        return ResponseEntity.ok().build()
    }
}
