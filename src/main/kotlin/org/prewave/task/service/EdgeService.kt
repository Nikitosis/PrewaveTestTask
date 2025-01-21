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

    //TODO: maybe add transactions in case of concurrent calls?
    fun removeEdge(edgeDto: EdgeDTO) {
        log.info("Removing edge: {}", edgeDto)
        val existingEdge = edgeRepository.findByFromIdAndToId(edgeDto.fromId!!, edgeDto.toId!!)
        if (existingEdge == null) {
            throw EntityNotFoundException("Edge with fromId=${edgeDto.fromId}; toId=${edgeDto.toId} not found")
        }

        edgeRepository.removeEdge(edgeDto.fromId!!, edgeDto.toId!!)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
