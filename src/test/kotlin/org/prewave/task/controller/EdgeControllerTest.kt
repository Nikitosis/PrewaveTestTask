package org.prewave.task.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.prewave.task.entity.dto.EdgeDTO
import org.prewave.task.service.EdgeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post

@WebMvcTest(EdgeController::class)
class EdgeControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var edgeService: EdgeService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun createEdge() {
        val edgeDto = EdgeDTO(
            fromId = 1,
            toId = 2
        )

        mockMvc.post("/edges") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(edgeDto)
        }
            .andExpect {
                status { isOk() }
            }

        verify(edgeService).createEdge(edgeDto)
    }

    @Test
    fun removeEdge() {
        val fromId = 1
        val toId = 2

        mockMvc.delete("/edges") {
            contentType = MediaType.APPLICATION_JSON
            param("fromId", fromId.toString())
            param("toId", toId.toString())
        }
            .andExpect {
                status { isOk() }
            }

        verify(edgeService).removeEdge(fromId, toId)
    }
}
