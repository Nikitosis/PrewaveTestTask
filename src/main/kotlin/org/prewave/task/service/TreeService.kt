package org.prewave.task.service

import org.prewave.task.entity.dto.TreeDTO
import org.prewave.task.exception.InternalServerErrorException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpServerErrorException.InternalServerError

@Service
class TreeService(
    private val edgeService: EdgeService
) {

    fun getTree(parentNodeId: Int): TreeDTO {
        log.info("Getting tree for parentNodeId={}", parentNodeId)

        val rootTreeDto = TreeDTO().apply {
            this.nodeId = parentNodeId
            this.children = mutableListOf()
        }

        val nodeIdToTreeDto = mutableMapOf<Int, TreeDTO>()
        nodeIdToTreeDto.put(parentNodeId, rootTreeDto)

        var curLevelNodes = listOf(parentNodeId)

        while (true) {
            //TODO: add batching
            val curLevelEdges = edgeService.findAllByFromIds(curLevelNodes)
            if (curLevelEdges.isEmpty()) {
                break
            }

            curLevelEdges.forEach{ curEdge ->
                if (nodeIdToTreeDto.containsKey(curEdge.toId)) {
                    throw InternalServerErrorException("Cycle detected in a tree fromId=${curEdge.fromId}; toId=${curEdge.toId}")
                }

                val curTreeDto = TreeDTO().apply {
                    this.nodeId = curEdge.toId
                    this.children = mutableListOf()
                }
                nodeIdToTreeDto.put(curTreeDto.nodeId!!, curTreeDto)

                val curTreeDtoFrom = nodeIdToTreeDto.get(curEdge.fromId)!!
                val curTreeDtoTo = nodeIdToTreeDto.get(curEdge.toId)!!

                curTreeDtoFrom.children!!.add(curTreeDtoTo)
            }

            curLevelNodes = curLevelEdges.map { it.toId!! }
        }

        return nodeIdToTreeDto.get(parentNodeId)!!
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
