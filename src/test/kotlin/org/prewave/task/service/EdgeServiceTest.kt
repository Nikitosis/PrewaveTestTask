package org.prewave.task.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.prewave.task.entity.dto.EdgeDTO
import org.prewave.task.exception.EntityAlreadyExistsException
import org.prewave.task.exception.EntityNotFoundException
import org.prewave.task.repository.EdgeRepository
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class EdgeServiceTest {
    @Mock
    lateinit var edgeRepository: EdgeRepository

    @InjectMocks
    lateinit var edgeService: EdgeService

    @Test
    fun createEdge() {
        val edgeDto = EdgeDTO(
            fromId = 1,
            toId = 2
        )

        edgeService.createEdge(edgeDto)

        verify(edgeRepository).addEdge(edgeDto.fromId!!, edgeDto.toId!!)
    }

    @Test
    fun createEdge_alreadyExists() {
        val edgeDto = EdgeDTO(
            fromId = 1,
            toId = 2
        )

        `when`(edgeRepository.findByFromIdAndToId(edgeDto.fromId!!, edgeDto.toId!!)).thenReturn(edgeDto)

        assertThrows<EntityAlreadyExistsException> {edgeService.createEdge(edgeDto)}
        verify(edgeRepository, times(0)).addEdge(edgeDto.fromId!!, edgeDto.toId!!)
    }

    @Test
    fun removeEdge() {

        val edgeDto = EdgeDTO(
            fromId = 1,
            toId = 2
        )

        `when`(edgeRepository.findByFromIdAndToId(edgeDto.fromId!!, edgeDto.toId!!)).thenReturn(edgeDto)

        edgeService.removeEdge(edgeDto.fromId!!, edgeDto.toId!!)

        verify(edgeRepository).removeEdge(edgeDto.fromId!!, edgeDto.toId!!)
    }

    @Test
    fun createEdge_notFound() {
        val edgeDto = EdgeDTO(
            fromId = 1,
            toId = 2
        )

        assertThrows<EntityNotFoundException> {edgeService.removeEdge(edgeDto.fromId!!, edgeDto.toId!!)}
    }
}
