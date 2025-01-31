package org.prewave.task.service

import org.prewave.task.entity.dto.EdgeDTO
import org.prewave.task.entity.dto.TreeDTO
import org.prewave.task.entity.generated.tables.records.EdgeRecord
import org.prewave.task.exception.EntityAlreadyExistsException
import org.prewave.task.exception.EntityNotFoundException
import org.prewave.task.repository.EdgeRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EdgeService(
    private val edgeRepository: EdgeRepository
) {

    fun isNodeExist(nodeId: Int): Boolean {
        return edgeRepository.isNodeExist(nodeId)
    }

    fun findAllByFromIds(fromIds: List<Int>): List<EdgeDTO> {
        return edgeRepository.findAllByFromIds(fromIds)
    }

    fun createEdge(edgeDto: EdgeDTO) {
        log.info("Creating edge: {}", edgeDto)
        val existingEdge = edgeRepository.findByFromIdAndToId(edgeDto.fromId!!, edgeDto.toId!!)
        if (existingEdge != null) {
            throw EntityAlreadyExistsException("Edge with fromId=${edgeDto.fromId}; toId=${edgeDto.toId} already exists")
        }

        edgeRepository.addEdge(edgeDto.fromId!!, edgeDto.toId!!)
    }

    fun removeEdge(fromId: Int, toId: Int) {
        log.info("Removing edge fromId={}; toId={}", fromId, toId)
        val existingEdge = edgeRepository.findByFromIdAndToId(fromId, toId)
        if (existingEdge == null) {
            throw EntityNotFoundException("Edge with fromId=${fromId}; toId=${toId} not found")
        }

        edgeRepository.removeEdge(fromId, toId)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
