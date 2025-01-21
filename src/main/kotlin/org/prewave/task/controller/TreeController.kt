package org.prewave.task.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.prewave.task.entity.dto.EdgeDTO
import org.prewave.task.entity.dto.TreeDTO
import org.prewave.task.service.EdgeService
import org.prewave.task.service.TreeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/tree")
class TreeController(
    private val treeService: TreeService
) {
    @GetMapping
    fun getTree(@RequestParam parentNodeId: Int): ResponseEntity<TreeDTO> {
        val tree = treeService.getTree(parentNodeId)
        return ResponseEntity.ok(tree)
    }
}
