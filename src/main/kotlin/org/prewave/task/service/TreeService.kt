package org.prewave.task.service

import org.prewave.task.entity.dto.TreeDTO
import org.springframework.stereotype.Service

@Service
class TreeService(
    private val edgeService: EdgeService
) {

    fun getTree(parentNodeId: Int): TreeDTO {
        val parentTreeDto = TreeDTO().apply {
            this.nodeId = parentNodeId
            this.children = mutableListOf()
        }

        val nodeIdToTreeDto = mutableMapOf<Int, TreeDTO>()
        nodeIdToTreeDto.put(parentNodeId, parentTreeDto)

        var curLevelNodes = listOf(parentNodeId)

        while (true) {
            //TODO: add batching
            val curLevelEdges = edgeService.findAllByFromIds(curLevelNodes)
            if (curLevelEdges.isEmpty()) {
                break
            }

            curLevelEdges.forEach{ curEdge ->
                if (!nodeIdToTreeDto.containsKey(curEdge.fromId)) {
                    val curTreeDto = TreeDTO().apply {
                        this.nodeId = curEdge.fromId
                        this.children = mutableListOf()
                    }
                    nodeIdToTreeDto.put(curTreeDto.nodeId!!, curTreeDto)
                }
                if (!nodeIdToTreeDto.containsKey(curEdge.toId)) {
                    val curTreeDto = TreeDTO().apply {
                        this.nodeId = curEdge.toId
                        this.children = mutableListOf()
                    }
                    nodeIdToTreeDto.put(curTreeDto.nodeId!!, curTreeDto)
                }

                val curTreeDtoFrom = nodeIdToTreeDto.get(curEdge.fromId)!!
                val curTreeDtoTo = nodeIdToTreeDto.get(curEdge.toId)!!

                curTreeDtoFrom.children!!.add(curTreeDtoTo)
            }

            curLevelNodes = curLevelEdges.map { it.toId!! }
        }

        return nodeIdToTreeDto.get(parentNodeId)!!
    }
}
