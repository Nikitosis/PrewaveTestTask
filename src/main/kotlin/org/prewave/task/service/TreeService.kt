package org.prewave.task.service

import org.prewave.task.entity.dto.EdgeDTO
import org.prewave.task.entity.dto.TreeDTO
import org.prewave.task.exception.EntityNotFoundException
import org.prewave.task.exception.InternalServerErrorException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpServerErrorException.InternalServerError

@Service
class TreeService(
    private val edgeService: EdgeService
) {

    /**
     * Starting from parentNodeId, receive all connected edges which are coming from the node.
     * Then for all the edges get destination nodes.
     * Then for the destination nodes receive all connected edges which are coming from the nodes.
     * Then for all the edges get destination nodes.
     * Repeat the process until there are no more incoming edges.
     * Basically it is very similar to BFS algorithm
     */
    fun getTree(parentNodeId: Int): TreeDTO {
        log.info("Getting tree for parentNodeId={}", parentNodeId)

        if (!edgeService.isNodeExist(parentNodeId)) {
            throw EntityNotFoundException("No node found with parentNodeId=$parentNodeId")
        }

        val rootTreeDto = TreeDTO().apply {
            this.nodeId = parentNodeId
            this.children = mutableListOf()
        }

        val nodeIdToTreeDto = mutableMapOf<Int, TreeDTO>()
        nodeIdToTreeDto.put(parentNodeId, rootTreeDto)

        var curLevelNodes = listOf(parentNodeId)

        while (true) {
            val curLevelEdges = findEdgesByFromIdsBatched(curLevelNodes)
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

    /**
     * As we can have up to several millions of fromIds, we should limit their amount when making SQL request
     * Therefore, we break the whole list into smaller pieces and make several smaller SQL requests
     */
    private fun findEdgesByFromIdsBatched(fromIds: List<Int>): List<EdgeDTO> {
        val batchesFromIds = fromIds.chunked(EDGES_REQUEST_BATCH_SIZE)

        return batchesFromIds.map { curBatchFromIds ->
            edgeService.findAllByFromIds(curBatchFromIds)
        }.flatten()
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)

        private const val EDGES_REQUEST_BATCH_SIZE = 10000
    }
}
